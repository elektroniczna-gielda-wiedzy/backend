package backend.repositories;

import backend.model.dao.EntryDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends JpaRepository<EntryDao, Integer> {

    EntryDao getEntryDaoByEntryId(Integer entryId);

}
