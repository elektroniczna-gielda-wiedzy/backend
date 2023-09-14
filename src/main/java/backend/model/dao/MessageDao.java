package backend.model.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "messages")
@Getter
@Setter
public class MessageDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private ChatDao chatDao;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User senderUser;

    @Column(name = "content")
    private String content;

    @Column(name = "date_sent")
    private Timestamp dateSent;
}
