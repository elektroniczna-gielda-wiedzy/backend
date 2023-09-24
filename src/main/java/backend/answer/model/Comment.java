package backend.answer.model;

import backend.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;

@Entity
@Table(name = "Comment")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Timestamp createdAt;

    public static Specification<Comment> hasAnswerId(Integer answerId) {
        if (answerId == null) {
            return (comment, cq, cb) -> cb.conjunction();
        }
        return (comment, cq, cb) -> cb.equal(comment.get("answer"), answerId);
    }
}
