package backend.model.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "Entry")
@Getter
@Setter
public class EntryDao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entry_id_sequence")
    @SequenceGenerator(name = "entry_id_sequence", sequenceName = "entry_answer_id_sequence", allocationSize = 1, initialValue = 1000)
    @Column(name = "entry_id")
    private Integer entryId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDao author;

    @ManyToOne
    @JoinColumn(name = "entry_type_id")
    private EntryTypeDao entryType;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToMany
    @JoinTable(
            name = "VotedItem",
            joinColumns = {@JoinColumn(name = "voted_item_id")},
            inverseJoinColumns = { @JoinColumn(name = "vote_id")}
    )
    private Set<VoteDao> votes;

    @ManyToMany
    @JoinTable(
            name = "ImageEntry",
            joinColumns = {@JoinColumn(name = "image_item_id")},
            inverseJoinColumns = { @JoinColumn(name = "image_id")}
    )
    private Set<ImageDao> images;

    @ManyToMany
    @JoinTable(
            name = "EntryCategory",
            joinColumns = {@JoinColumn(name = "entry_id")},
            inverseJoinColumns = { @JoinColumn(name = "category_id")}
    )
    private Set<CategoryDao> categories;

    @ManyToMany
    @JoinTable(
            name = "Favorites",
            joinColumns = {@JoinColumn(name = "entry_id")},
            inverseJoinColumns = { @JoinColumn(name = "user_id")}
    )
    private Set<UserDao> likedBy;

    @OneToMany(mappedBy = "entry")
    private Set<AnswerDao> answers;

}
