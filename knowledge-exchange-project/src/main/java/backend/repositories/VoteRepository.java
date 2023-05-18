package backend.repositories;

import backend.model.dao.VoteDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<VoteDao, Integer> {
}
