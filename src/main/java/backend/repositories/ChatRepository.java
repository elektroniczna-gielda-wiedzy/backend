package backend.repositories;

import backend.model.dao.Chat;
import backend.model.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer>, JpaSpecificationExecutor<Chat> {
    List<Chat> findChatsByUserOneOrUserTwo(User userOneDao, User userTwoDao);

    List<Chat> findChatsByUserOneInAndUserTwoIn(Collection<User> userOneDao,
                                                Collection<User> userTwoDao);
}
