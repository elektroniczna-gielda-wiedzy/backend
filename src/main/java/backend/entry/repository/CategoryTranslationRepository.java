package backend.entry.repository;

import backend.entry.model.CategoryTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryTranslationRepository extends JpaRepository<CategoryTranslation, Integer> {
}
