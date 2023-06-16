package backend.repositories;

import backend.model.dao.AnswerDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerDao, Integer> {
}
