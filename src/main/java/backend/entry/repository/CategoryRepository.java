package backend.entry.repository;

import backend.entry.model.Category;
import backend.entry.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    Set<Category> getCategoriesByIdIsIn(List<Integer> categoryIds);
}
