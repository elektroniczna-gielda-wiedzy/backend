package backend.entry.model;

import backend.answer.model.Answer;
import backend.common.model.Image;
import backend.user.model.User;
import backend.common.model.Vote;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Join;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "Entry")
@Getter
@Setter
public class Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entry_id_sequence")
    @SequenceGenerator(name = "entry_id_sequence",
                       sequenceName = "entry_answer_id_sequence",
                       allocationSize = 1,
                       initialValue = 1000)
    @Column(name = "entry_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "entry_type_id")
    private EntryType type;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDeleted = false;

    @ManyToMany
    @JoinTable(name = "VoteEntry",
               joinColumns = {@JoinColumn(name = "voted_item_id")},
               inverseJoinColumns = {@JoinColumn(name = "vote_id")})
    private Set<Vote> votes;

    @ManyToMany
    @JoinTable(name = "ImageEntry",
               joinColumns = {@JoinColumn(name = "image_item_id")},
               inverseJoinColumns = {@JoinColumn(name = "image_id")})
    private Set<Image> images;

    @ManyToMany
    @JoinTable(name = "EntryCategory",
               joinColumns = {@JoinColumn(name = "entry_id")},
               inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private Set<Category> categories;

    @ManyToMany
    @JoinTable(name = "Favorites",
               joinColumns = {@JoinColumn(name = "entry_id")},
               inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> likedBy;

    @OneToMany(mappedBy = "entry")
    private Set<Answer> answers;

    public void addVote(User user, Integer value) {
        Optional<Vote> voteOptional = this.votes.stream()
                .filter(v -> v.getUser().getId().equals(user.getId()))
                .findFirst();
        Vote vote;

        if (voteOptional.isPresent()) {
            vote = voteOptional.get();
            this.votes.remove(vote);
            vote.setValue(value);
        } else {
            vote = new Vote();
            vote.setUser(user);
            vote.setValue(value);
        }

        this.votes.add(vote);
    }

    public static Specification<Entry> titleContains(String query) {
        if (query == null) {
            return (entry, cq, cb) -> cb.conjunction();
        }
        return (entry, cq, cb) -> cb.like(entry.get("title"), "%" + query + "%");
    }

    public static Specification<Entry> contentContains(String query) {
        if (query == null) {
            return (entry, cq, cb) -> cb.conjunction();
        }
        return (entry, cq, cb) -> cb.like(entry.get("content"), "%" + query + "%");
    }

    public static Specification<Entry> hasType(Integer type) {
        if (type == null) {
            return (entry, cq, cb) -> cb.conjunction();
        }
        return (entry, cq, cb) -> cb.equal(entry.get("type"), type);
    }

    public static Specification<Entry> hasAuthor(Integer author) {
        if (author == null) {
            return (entry, cq, cb) -> cb.conjunction();
        }
        return (entry, cq, cb) -> cb.equal(entry.get("author"), author);
    }

    public static Specification<Entry> favoriteBy(Integer user) {
        if (user == null) {
            return (entry, cq, cb) -> cb.conjunction();
        }
        return (entry, cq, cb) -> {
            Join<Entry, User> entryUser = entry.join("likedBy");
            return cb.equal(entryUser.get("id"), user);
        };
    }

    public static Specification<Entry> isNotDeleted() {
        return (entry, cq, cb) -> cb.isFalse(entry.get("isDeleted"));
    }
}
