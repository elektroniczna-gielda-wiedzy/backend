package backend.repositories;

import backend.model.dao.EntryDao;
import backend.model.dao.UserDao;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<EntryDao, Integer> {

}
