package backend.repositories;

import backend.model.dao.CategoryDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryDao, Integer> {
    Set<CategoryDao> getCategoryDaosByCategoryIdIsIn(Collection<Integer> categoryId);
}
