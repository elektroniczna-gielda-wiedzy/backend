package backend.repositories;

import backend.model.dao.ChatDao;
import backend.model.dao.MessageDao;
import backend.model.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface MessageRepository extends JpaRepository<MessageDao, Integer> {
    List<MessageDao> findMessageDaosByChatDao(ChatDao chatDao);

    List<MessageDao> findMessageDaosBySenderUserAndDateSentGreaterThan(User senderUser, Timestamp dateSent);
}
