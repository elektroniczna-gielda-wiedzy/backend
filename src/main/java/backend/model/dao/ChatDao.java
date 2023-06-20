package backend.model.dao;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Chat")
@Getter
@Setter
public class ChatDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Integer chatId;
    @ManyToOne
    @JoinColumn(name = "user_one_id")
    private UserDao userOneDao;
    @ManyToOne
    @JoinColumn(name = "user_two_id")
    private UserDao userTwoDao;

}
