package backend.common.service;

import backend.answer.model.Answer;
import backend.answer.repository.AnswerRepository;
import backend.common.model.Vote;
import backend.common.repository.VoteRepository;
import backend.entry.model.Entry;
import backend.entry.repository.EntryRepository;
import backend.user.model.User;
import backend.user.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static backend.answer.model.Answer.hasEntryId;
import static backend.answer.model.Answer.isTopAnswer;

@Service
public class VoteService {
    private final EntryRepository entryRepository;

    private final UserRepository userRepository;

    private final VoteRepository voteRepository;

    private final AnswerRepository answerRepository;

    public VoteService(EntryRepository entryRepository, UserRepository userRepository, VoteRepository voteRepository,
                       AnswerRepository answerRepository) {
        this.entryRepository = entryRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.answerRepository = answerRepository;
    }

    public void voteForEntry(Integer entryId, Integer userId, Integer value) {
        Entry entry = this.entryRepository.findById(entryId).orElseThrow(
                () -> new GenericServiceException(String.format("Entry with id = %d does not exist", entryId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        Optional<Vote> voteOptional = entry.getVotes().stream()
                .filter(v -> v.getUser().getId().equals(userId))
                .findFirst();

        Vote vote;

        if (voteOptional.isPresent()) {
            vote = voteOptional.get();
            vote.setValue(value);
        } else {
            vote = new Vote();
            vote.setUser(user);
            vote.setValue(value);
            entry.getVotes().add(vote);
        }

        try {
            this.voteRepository.save(vote);
            this.entryRepository.save(entry);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }

    public void setFavoriteStatus(Integer entryId, Integer userId, Integer opcode) {
        Entry entry = this.entryRepository.findById(entryId).orElseThrow(
                () -> new GenericServiceException(String.format("Entry with id = %d does not exist", entryId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        Set<Entry> favorites = user.getFavorites();
        Set<User> linkedBy = entry.getLikedBy();
        boolean isInFavorites = favorites.contains(entry);

        switch (opcode) {
            case 1 -> {
                /* Add to favorites */
                if (isInFavorites) {
                    return;  /* XXX: Error? */
                }
                favorites.add(entry);
                linkedBy.add(user);
            }
            case -1 -> {
                /* Remove from favorites */
                if (!isInFavorites) {
                    return; /* XXX: Error? */
                }
                favorites.remove(entry);
                linkedBy.remove(user);
            }
            default -> throw new GenericServiceException("Invalid opcode");
        }

        user.setFavorites(favorites);
        entry.setLikedBy(linkedBy);

        try {
            this.userRepository.save(user);
            this.entryRepository.save(entry);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }

    public void voteForAnswer(Integer answerId, Integer userId, Integer value) {
        Answer answer = this.answerRepository.findById(answerId).orElseThrow(
                () -> new GenericServiceException(String.format("Answer with id = %d does not exist", answerId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        Optional<Vote> voteOptional = answer.getVotes().stream()
                .filter(v -> v.getUser().getId().equals(userId))
                .findFirst();

        Vote vote;

        if (voteOptional.isPresent()) {
            vote = voteOptional.get();
            vote.setValue(value);
        } else {
            vote = new Vote();
            vote.setUser(user);
            vote.setValue(value);
            answer.getVotes().add(vote);
        }

        try {
            this.voteRepository.save(vote);
            this.answerRepository.save(answer);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }

    public void markTopAnswer(Integer entryId, Integer answerId, Integer value, Integer userId) {
        Answer answer = this.answerRepository.findById(answerId).orElseThrow(
                () -> new GenericServiceException(String.format("Answer with id = %d does not exist", answerId)));

        Entry entry = this.entryRepository.findById(entryId).orElseThrow(
                () -> new GenericServiceException(String.format("Entry with id = %d does not exist", entryId)));

        if (!entry.getAuthor().getId().equals(userId)) {
            throw new GenericServiceException("Only the entry creator can set and unset top answers");
        }

        if (answer.getUser().getId().equals(userId)) {
            throw new GenericServiceException("You cannot mark your answer as the best");
        }

        List<Answer> topAnswers = this.answerRepository.findAll(hasEntryId(entryId).and(isTopAnswer()));
        topAnswers.forEach((entity) -> entity.setIsTopAnswer(false));

        switch (value) {
            case 1 -> answer.setIsTopAnswer(true);
            case -1 -> answer.setIsTopAnswer(false);
            default -> throw new GenericServiceException("Invalid value sent in request body");
        }

        try {
            this.answerRepository.save(answer);
            this.answerRepository.saveAll(topAnswers);
        } catch (DataAccessException e) {
            throw new GenericServiceException(e.getMessage());
        }
    }
}
