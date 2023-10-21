package backend.answer.service;

import backend.answer.model.Answer;
import backend.common.model.Vote;
import backend.common.service.ImageService;
import backend.entry.model.Entry;
import backend.entry.model.EntryType;
import backend.user.model.User;
import backend.answer.repository.AnswerRepository;
import backend.entry.repository.EntryRepository;
import backend.user.repository.UserRepository;
import backend.common.service.GenericServiceException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.BooleanComparator;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static backend.answer.model.Answer.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    private final EntryRepository entryRepository;

    private final UserRepository userRepository;

    private final ImageService imageService;

    public AnswerService(AnswerRepository answerRepository,
                         EntryRepository entryRepository,
                         UserRepository userRepository,
                         ImageService imageService) {
        this.answerRepository = answerRepository;
        this.entryRepository = entryRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    public List<Answer> getAnswers(Integer entryId) {
        Entry entry = this.entryRepository.findById(entryId).orElseThrow(
                () -> new GenericServiceException(String.format("Entry with id = %d does not exist", entryId)));

        return this.answerRepository.findAll(where(
            hasEntryId(entryId).and(isNotDeleted())
        )).stream().sorted(
                Comparator.comparing(Answer::getIsTopAnswer).thenComparing
                        (answer -> answer.getVotes().stream().map(Vote::getValue)
                                .reduce(0, Integer::sum)).reversed().thenComparing(Answer::getCreatedAt)
        ).toList();
    }

    public Answer createAnswer(Integer entryId, Integer userId, String content, String imageFilename, byte[] imageData) {
        Entry entry = this.entryRepository.findById(entryId).orElseThrow(
                () -> new GenericServiceException(String.format("Entry with id = %d does not exist", entryId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        if (!entry.getType().equals(EntryType.POST)) {
            throw new GenericServiceException("Answers can only be created for entries of \"Post\" type");
        }

        Timestamp timeNow = Timestamp.from(Instant.now());

        Answer answer = new Answer();
        answer.setEntry(entry);
        answer.setUser(user);
        answer.setContent(content);
        answer.setCreatedAt(timeNow);
        answer.setUpdatedAt(timeNow);
        answer.setVotes(Set.of());
        answer.setIsDeleted(false);
        answer.setIsTopAnswer(false);

        if (imageFilename != null) {
            answer.setImage(this.imageService.createImage(imageFilename, imageData));
        }

        try {
            this.answerRepository.save(answer);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return answer;
    }

    public Answer editAnswer(Integer answerId, Integer userId, String content, String imageFilename, byte[] imageData) {
        Answer answer = this.answerRepository.findById(answerId).orElseThrow(
                () -> new GenericServiceException(String.format("Answer with id = %d does not exist", answerId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        if (!answer.getUser().getId().equals(userId) && !user.getIsAdmin()) {
            throw new GenericServiceException("You are not allowed to edit this answer");
        }

        if (content != null) {
            answer.setContent(content);
        }

        if (imageFilename != null) {
            String currentImage = answer.getImage();
            if (currentImage != null) {
                this.imageService.deleteImage(currentImage);
            }
            answer.setImage(this.imageService.createImage(imageFilename, imageData));
        }

        answer.setUpdatedAt(Timestamp.from(Instant.now()));

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
