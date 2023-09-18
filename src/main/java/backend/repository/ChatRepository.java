package backend.repository;

import backend.model.Chat;
import backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer>, JpaSpecificationExecutor<Chat> {
}
