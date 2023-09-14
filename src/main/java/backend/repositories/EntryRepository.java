package backend.repositories;

import backend.model.dao.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Integer>, JpaSpecificationExecutor<Entry> {
}
