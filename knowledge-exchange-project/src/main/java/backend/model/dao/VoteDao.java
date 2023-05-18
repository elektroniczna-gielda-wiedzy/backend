package backend.model.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Vote")
@Getter
@Setter
public class VoteDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Integer voteId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDao user;

    @Column(name = "value")
    private Integer value;
}
