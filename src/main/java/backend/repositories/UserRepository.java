package backend.repositories;

import backend.model.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDao, Integer> {
    UserDao findUserDaoByUserId(Integer userId);

    UserDao findUserDaoByEmail(String email);
}
