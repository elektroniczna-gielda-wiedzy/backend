package backend.model.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "Comment")
@Getter
@Setter
public class CommentDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private AnswerDao answer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
