package backend.entry.service;

import backend.common.model.Language;
import backend.common.repository.LanguageRepository;
import backend.common.service.GenericServiceException;
import backend.entry.model.Category;
import backend.entry.model.CategoryTranslation;
import backend.entry.model.CategoryTranslationDto;
import backend.entry.model.CategoryType;
import backend.entry.repository.CategoryRepository;
import backend.entry.repository.CategoryTranslationRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryTranslationRepository categoryTranslationRepository;

    private final LanguageRepository languageRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryTranslationRepository categoryTranslationRepository,
                           LanguageRepository languageRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryTranslationRepository = categoryTranslationRepository;
        this.languageRepository = languageRepository;
    }

    public List<Category> getCategories() {
        return this.categoryRepository.findAll();
    }

    public Category addCategory(Integer typeId, List<CategoryTranslationDto> translations, Integer parentId) {
        CategoryType type = CategoryType.valueOf(typeId).orElseThrow(
                () -> new GenericServiceException("Invalid category type"));

        Category parent = this.categoryRepository.findById(parentId).orElseThrow(
                () -> new GenericServiceException(String.format("Category with id = %d does not exist", parentId)));

        Category category = new Category();
        category.setCategoryType(type);
        category.setParentCategory(parent);
        category.setIsDeleted(false);

        try {
            this.categoryRepository.save(category);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        Set<CategoryTranslation> categoryTranslations = new HashSet<>();

        for (CategoryTranslationDto translation: translations) {
            CategoryTranslation categoryTranslation = new CategoryTranslation();
            categoryTranslation.setCategory(category);
            categoryTranslation.setTranslation(translation.getTranslation());
            categoryTranslation.setLanguage(this.languageRepository.findById(translation.getLanguageId()).orElseThrow(
                    () -> new GenericServiceException(String.format("Language with id = %d does not exist",
                                                                    translation.getLanguageId()))));

            try {
                this.categoryTranslationRepository.save(categoryTranslation);
            } catch (DataAccessException exception) {
                throw new GenericServiceException(exception.getMessage());
            }

            categoryTranslations.add(categoryTranslation);
        }

        category.setCategoryTranslations(categoryTranslations);

        try {
            this.categoryRepository.save(category);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return category;
    }

    public Category editCategory() {
        return null;
    }

    public void deleteCategory(Integer categoryId) {
    }
}
