package backend.common.service;

import backend.common.model.Vote;
import backend.common.repository.VoteRepository;
import backend.entry.model.Entry;
import backend.entry.repository.EntryRepository;
import backend.user.model.User;
import backend.user.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {
    private final EntryRepository entryRepository;

    private final UserRepository userRepository;

    private final VoteRepository voteRepository;

    public VoteService(EntryRepository entryRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.entryRepository = entryRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
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

    public void setFavoriteStatus(Integer entryId, Integer value) {
        // TODO
    }

    public void voteForAnswer(Integer entryId, Integer answerId, Integer value) {
        // TODO
    }
}
