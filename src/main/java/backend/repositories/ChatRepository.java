package backend.repositories;

import backend.model.dao.ChatDao;
import backend.model.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ChatRepository extends JpaRepository<ChatDao, Integer> {
    ChatDao findChatDaoByChatId(Integer chatId);

    List<ChatDao> findChatDaosByUserOneDaoOrUserTwoDao(UserDao userOneDao, UserDao userTwoDao);

    List<ChatDao> findChatDaosByUserOneDaoInAndUserTwoDaoIn(Collection<UserDao> userOneDao,
                                                                   Collection<UserDao> userTwoDao);
}
