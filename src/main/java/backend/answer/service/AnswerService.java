package backend.answer.service;

import backend.answer.model.Answer;
import backend.entry.model.Entry;
import backend.user.model.User;
import backend.answer.repository.AnswerRepository;
import backend.entry.repository.EntryRepository;
import backend.user.repository.UserRepository;
import backend.common.service.GenericServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;

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
        // TODO
        return null;
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

    public Answer editAnswer(Integer entryId, Integer userId, Integer answerId, String Content) {
        // TODO
        return null;
    }

    public void deleteAnswer(Integer entryId, Integer answerId) {
        // TODO
    }
}
