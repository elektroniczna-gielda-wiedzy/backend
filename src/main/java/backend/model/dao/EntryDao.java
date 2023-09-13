package backend.model.dao;

import backend.model.CategoryType;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Join;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Entry")
@Getter
@Setter
public class EntryDao {
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
    private UserDao author;

    @ManyToOne
    @JoinColumn(name = "entry_type_id")
    private EntryTypeDao type;

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
    @JoinTable(name = "VotedItem",
               joinColumns = {@JoinColumn(name = "voted_item_id")},
               inverseJoinColumns = {@JoinColumn(name = "vote_id")})
    private Set<VoteDao> votes;

    @ManyToMany
    @JoinTable(name = "ImageEntry",
               joinColumns = {@JoinColumn(name = "image_item_id")},
               inverseJoinColumns = {@JoinColumn(name = "image_id")})
    private Set<ImageDao> images;

    @ManyToMany
    @JoinTable(name = "EntryCategory",
               joinColumns = {@JoinColumn(name = "entry_id")},
               inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private Set<CategoryDao> categories;

    @ManyToMany
    @JoinTable(name = "Favorites",
               joinColumns = {@JoinColumn(name = "entry_id")},
               inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<UserDao> likedBy;

    @OneToMany(mappedBy = "entry")
    private Set<AnswerDao> answers;

    public static Specification<EntryDao> titleContains(String query) {
        if (query == null) {
            return (entry, cq, cb) -> cb.conjunction();
        }
        return (entry, cq, cb) -> cb.like(entry.get("title"), "%" + query + "%");
    }

    public static Specification<EntryDao> contentContains(String query) {
        if (query == null) {
            return (entry, cq, cb) -> cb.conjunction();
        }
        return (entry, cq, cb) -> cb.like(entry.get("content"), "%" + query + "%");
    }

    public static Specification<EntryDao> hasType(Integer type) {
        if (type == null) {
            return (entry, cq, cb) -> cb.conjunction();
        }
        return (entry, cq, cb) -> cb.equal(entry.get("type"), type);
    }

    public static Specification<EntryDao> hasAuthor(Integer author) {
        if (author == null) {
            return (entry, cq, cb) -> cb.conjunction();
        }
        return (entry, cq, cb) -> cb.equal(entry.get("author"), author);
    }

    public static Specification<EntryDao> favoriteBy(Integer user) {
        if (user == null) {
            return (entry, cq, cb) -> cb.conjunction();
        }
        return (entry, cq, cb) -> {
            Join<EntryDao, UserDao> entryUser = entry.join("likedBy");
            return cb.equal(entryUser.get("id"), user);
        };
    }
}
