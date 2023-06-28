package backend.model.dao;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

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

    @Column(name = "user_one_last_read")
    private Timestamp userOneLastRead;

    @Column(name = "user_two_last_read")
    private Timestamp userTwoLastRead;

}
