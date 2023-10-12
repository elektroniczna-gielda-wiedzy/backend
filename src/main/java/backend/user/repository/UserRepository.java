package backend.user.repository;

import backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findUserDaoByEmail(String email);

    @Query("SELECT u from User u where CONCAT(upper(u.firstName),' ', upper(u.lastName)) LIKE %:query% or " +
            "CONCAT(upper(u.lastName),' ', upper(u.firstName)) LIKE %:query%")
    List<User> findUsersByQuery(String query);
}
