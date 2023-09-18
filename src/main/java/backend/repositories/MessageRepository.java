package backend.repositories;

import backend.model.dao.Chat;
import backend.model.dao.Message;
import backend.model.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findMessagesByChat(Chat chat);

    List<Message> findMessagesBySenderUserAndDateSentGreaterThan(User senderUser, Timestamp dateSent);
}
