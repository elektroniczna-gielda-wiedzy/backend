package backend.repository;

import backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Set<Category> getCategoriesByIdIsIn(List<Integer> categoryIds);
}
