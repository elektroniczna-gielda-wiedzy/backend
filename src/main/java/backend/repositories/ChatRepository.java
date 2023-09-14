package backend.repositories;

import backend.model.dao.ChatDao;
import backend.model.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ChatRepository extends JpaRepository<ChatDao, Integer> {
    ChatDao findChatDaoByChatId(Integer chatId);

    List<ChatDao> findChatDaosByUserOneDaoOrUserTwoDao(User userOneDao, User userTwoDao);

    List<ChatDao> findChatDaosByUserOneDaoInAndUserTwoDaoIn(Collection<User> userOneDao,
                                                                   Collection<User> userTwoDao);
}
