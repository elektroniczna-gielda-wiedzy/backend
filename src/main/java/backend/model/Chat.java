package backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "Chat")
@Getter
@Setter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_one_id")
    private User userOne;

    @ManyToOne
    @JoinColumn(name = "user_two_id")
    private User userTwo;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    @Column(name = "user_one_last_read")
    private Timestamp userOneLastRead;

    @Column(name = "user_two_last_read")
    private Timestamp userTwoLastRead;

    public User getOppositeUser(Integer userId) {
        return userId.equals(this.getUserOne().getId()) ? this.getUserOne() : this.getUserTwo();
    }

    public Timestamp getLastReadForUser(Integer userId) {
        return userId.equals(this.getUserOne().getId()) ? this.getUserOneLastRead() : this.getUserTwoLastRead();
    }

    public static Specification<Chat> userOneIs(Integer userId) {
        if (userId == null) {
            return (chat, cq, cb) -> cb.conjunction();
        }
        return (chat, cq, cb) -> cb.equal(chat.get("userOne"), userId);
    }

    public static Specification<Chat> userTwoIs(Integer userId) {
        if (userId == null) {
            return (chat, cq, cb) -> cb.conjunction();
        }
        return (chat, cq, cb) -> cb.equal(chat.get("userTwo"), userId);
    }

    public static Specification<Chat> userOneIsIn(List<Integer> userIds) {
        return (chat, cq, cb) -> chat.get("userOne").in(userIds);
    }

    public static Specification<Chat> userTwoIsIn(List<Integer> userIds) {
        return (chat, cq, cb) -> chat.get("userTwo").in(userIds);
    }
}
