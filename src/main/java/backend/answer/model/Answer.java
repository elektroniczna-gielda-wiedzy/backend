package backend.answer.model;

import backend.entry.model.Entry;
import backend.common.model.Image;
import backend.user.model.User;
import backend.common.model.Vote;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "Answer")
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answer_id_sequence")
    @SequenceGenerator(name = "answer_id_sequence",
                       sequenceName = "entry_answer_id_sequence",
                       allocationSize = 1,
                       initialValue = 1000)
    @Column(name = "answer_id")
    private Integer id;

    @JoinColumn(name = "entry_id")
    @ManyToOne
    private Entry entry;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "top_answer")
    private Boolean isTopAnswer;

    @OneToMany(mappedBy = "answer")
    private Set<Comment> comments;

    @ManyToMany
    @JoinTable(name = "VoteAnswer",
               joinColumns = {@JoinColumn(name = "voted_item_id")},
               inverseJoinColumns = {@JoinColumn(name = "vote_id")})
    private Set<Vote> votes;

    @ManyToMany
    @JoinTable(name = "ImageAnswer",
               joinColumns = {@JoinColumn(name = "image_item_id")},
               inverseJoinColumns = {@JoinColumn(name = "image_id")})
    private Set<Image> images;

    public static Specification<Answer> hasEntryId(Integer entryId) {
        if (entryId == null) {
            return (answer, cq, cb) -> cb.conjunction();
        }
        return (answer, cq, cb) -> cb.equal(answer.get("entry"), entryId);
    }

    public static Specification<Answer> isNotDeleted() {
        return (answer, cq, cb) -> cb.isFalse(answer.get("isDeleted"));
    }
}
