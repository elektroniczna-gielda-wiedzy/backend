package backend.entry.service;

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

import static backend.entry.model.Category.*;
import static org.springframework.data.jpa.domain.Specification.where;

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
        return this.categoryRepository.findAll(where(isNotDeleted()));
    }

    public Category createCategory(Integer typeId, List<CategoryTranslationDto> translations, Integer parentId) {
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
            categoryTranslations.add(this.createCategoryTranslation(category,
                                                                    translation.getLanguageId(),
                                                                    translation.getTranslation()));
        }
        category.setCategoryTranslations(categoryTranslations);

        try {
            this.categoryRepository.save(category);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return category;
    }

    public Category updateCategory(Integer categoryId, Integer typeId, List<CategoryTranslationDto> translations,
                                   Integer parentId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(
                () -> new GenericServiceException(String.format("Category with id = %d does not exist", categoryId)));

        if (typeId != null) {
            CategoryType type = CategoryType.valueOf(typeId).orElseThrow(
                    () -> new GenericServiceException("Invalid category type"));
            category.setCategoryType(type);
        }

        if (translations.size() > 0) {
            this.categoryTranslationRepository.deleteAll(category.getCategoryTranslations());

            Set<CategoryTranslation> categoryTranslations = new HashSet<>();
            for (CategoryTranslationDto translation: translations) {
                categoryTranslations.add(this.createCategoryTranslation(category,
                                                                        translation.getLanguageId(),
                                                                        translation.getTranslation()));
            }
            category.setCategoryTranslations(categoryTranslations);
        }

        if (parentId != null) {
            Category parent = this.categoryRepository.findById(parentId).orElseThrow(
                    () -> new GenericServiceException(String.format("Category with id = %d does not exist", parentId)));
            category.setParentCategory(parent);
        }

        try {
            this.categoryRepository.save(category);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return category;
    }

    public void deleteCategory(Integer categoryId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(
                () -> new GenericServiceException(String.format("Category with id = %d does not exist", categoryId)));

        category.setIsDeleted(true);

        try {
            this.categoryRepository.save(category);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }

    private CategoryTranslation createCategoryTranslation(Category category, Integer languageId, String translation) {
        CategoryTranslation categoryTranslation = new CategoryTranslation();

        categoryTranslation.setCategory(category);
        categoryTranslation.setTranslation(translation);
        categoryTranslation.setLanguage(this.languageRepository.findById(languageId).orElseThrow(
                () -> new GenericServiceException(String.format("Language with id = %d does not exist", languageId))));

        try {
            this.categoryTranslationRepository.save(categoryTranslation);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return categoryTranslation;
    }
}
