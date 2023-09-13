package backend.repositories;

import backend.model.dao.EntryDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<EntryDao, Integer>, JpaSpecificationExecutor<EntryDao> {
}
