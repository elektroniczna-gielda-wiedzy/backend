package backend.chat.repository;

import backend.chat.model.Message;
import backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findMessagesBySenderUserAndDateSentGreaterThan(User senderUser, Timestamp dateSent);
}
