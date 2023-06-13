package backend.model.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "Answer")
@Getter
@Setter
public class AnswerDao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answer_id_sequence")
    @SequenceGenerator(name = "answer_id_sequence", sequenceName = "entry_answer_id_sequence", allocationSize = 1, initialValue = 1000)
    @Column(name = "answer_id")
    private Integer answerId;

    @JoinColumn(name = "entry_id")
    @ManyToOne
    private EntryDao entry;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDao user;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "top_answer")
    private Boolean isTopAnswer;

    @ManyToMany
    @JoinTable(
            name = "VotedItem",
            joinColumns = {@JoinColumn(name = "voted_item_id")},
            inverseJoinColumns = { @JoinColumn(name = "vote_id")}
    )
    private Set<VoteDao> votes;

    @ManyToMany
    @JoinTable(
            name = "ImageItem",
            joinColumns = {@JoinColumn(name = "image_item_id")},
            inverseJoinColumns = { @JoinColumn(name = "image_id")}
    )
    private Set<ImageDao> images;


}
