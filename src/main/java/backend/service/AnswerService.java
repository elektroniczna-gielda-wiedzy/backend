package backend.service;

import backend.model.Answer;
import backend.model.Entry;
import backend.model.User;
import backend.repository.AnswerRepository;
import backend.repository.EntryRepository;
import backend.repository.ImageRepository;
import backend.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AnswerService {
    ImageRepository imageRepository;

    private final AnswerRepository answerRepository;

    private final EntryRepository entryRepository;

    private final UserRepository userRepository;

    public AnswerService(AnswerRepository answerRepository,
                         EntryRepository entryRepository,
                         UserRepository userRepository,
                         ImageRepository imageRepository) {
        this.answerRepository = answerRepository;
        this.entryRepository = entryRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public List<Answer> getAnswers(Integer entryId) {
        // TODO
        return null;
    }

    public Answer createAnswer(Integer entryId, Integer userId, String content) {
        Optional<Entry> entryOptional = this.entryRepository.findById(entryId);
        if (entryOptional.isEmpty()) {
            throw new GenericServiceException(String.format("Entry with id = %d does not exist", entryId));
        }
        Entry entry = entryOptional.get();

        Optional<User> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new GenericServiceException(String.format("User with id = %d does not exist", userId));
        }
        User user = userOptional.get();

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
