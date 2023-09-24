package backend.answer.service;

import backend.answer.model.Answer;
import backend.entry.model.Entry;
import backend.user.model.User;
import backend.answer.repository.AnswerRepository;
import backend.entry.repository.EntryRepository;
import backend.user.repository.UserRepository;
import backend.common.service.GenericServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static backend.answer.model.Answer.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    private final EntryRepository entryRepository;

    private final UserRepository userRepository;

    public AnswerService(AnswerRepository answerRepository,
                         EntryRepository entryRepository,
                         UserRepository userRepository) {
        this.answerRepository = answerRepository;
        this.entryRepository = entryRepository;
        this.userRepository = userRepository;
    }

    public List<Answer> getAnswers(Integer entryId) {
        Entry entry = this.entryRepository.findById(entryId).orElseThrow(
                () -> new GenericServiceException(String.format("Entry with id = %d does not exist", entryId)));

        return this.answerRepository.findAll(where(
            hasEntryId(entryId).and(isNotDeleted())
        ), Sort.by("votes").descending());
    }

    public Answer createAnswer(Integer entryId, Integer userId, String content) {
        Entry entry = this.entryRepository.findById(entryId).orElseThrow(
                () -> new GenericServiceException(String.format("Entry with id = %d does not exist", entryId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        Answer answer = new Answer();
        answer.setEntry(entry);
        answer.setUser(user);
        answer.setContent(content);
        answer.setCreatedAt(Timestamp.from(Instant.now()));
        answer.setVotes(Set.of());
        answer.setIsDeleted(false);
        answer.setIsTopAnswer(false);

        // TODO: Implement image.

        try {
            this.answerRepository.save(answer);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return answer;
    }

    public Answer editAnswer(Integer answerId, String content) {
        Answer answer = this.answerRepository.findById(answerId).orElseThrow(
                () -> new GenericServiceException(String.format("Answer with id = %d does not exist", answerId)));

        if (content != null) {
            answer.setContent(content);
        }

        try {
            this.answerRepository.save(answer);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return answer;
    }

    public void deleteAnswer(Integer answerId, Integer userId) {
        Answer answer = this.answerRepository.findById(answerId).orElseThrow(
                () -> new GenericServiceException(String.format("Answer with id = %d does not exist", answerId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        if (answer.getIsDeleted()) {
            throw new GenericServiceException(String.format("Answer with id = %d is already deleted", answerId));
        }

        if (!answer.getUser().getId().equals(userId) && !user.getIsAdmin()) {
            throw new GenericServiceException("You are not allowed to delete this answer");
        }

        answer.setIsDeleted(true);

        try {
            this.answerRepository.save(answer);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }
}
