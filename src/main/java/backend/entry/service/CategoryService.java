package backend.entry.service;

import backend.common.repository.LanguageRepository;
import backend.common.service.GenericServiceException;
import backend.entry.model.*;
import backend.entry.repository.CategoryRepository;
import backend.entry.repository.CategoryTranslationRepository;
import backend.entry.repository.EntryRepository;
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
    private final EntryRepository entryRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryTranslationRepository categoryTranslationRepository,
                           LanguageRepository languageRepository,
                           EntryRepository entryRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryTranslationRepository = categoryTranslationRepository;
        this.languageRepository = languageRepository;
        this.entryRepository = entryRepository;
    }

    public List<Category> getCategories(Integer statusId, Integer typeId, Integer parentId) {
        CategoryStatus status = CategoryStatus.valueOf(statusId).orElseThrow(
                () -> new GenericServiceException("Invalid category status"));
        List<Category> allCategories = this.categoryRepository.findAll(where(
                hasStatus(statusId)
                .and(hasType(typeId))
                .and(hasParent(parentId))
        ));
        return processCategoriesForConditions(allCategories, status);
    }

    private List<Category> processCategoriesForConditions(List<Category> allCategories, CategoryStatus status) {
        Set<Category> result = new HashSet<>(allCategories);
        for (Category category : allCategories) {
            if (category.getParent() == null || category.getParent().getStatus() == null) {
                continue;
            }
            CategoryStatus parentStatus = category.getParent().getStatus();

            if (status == CategoryStatus.ACTIVE) {
                if (parentStatus != CategoryStatus.ACTIVE) {
                    result.remove(category);
                }
            } else {
                if (parentStatus == CategoryStatus.ACTIVE) {
                    result.add(category.getParent());
                }
            }

        }
        return result.stream().toList();
    }

    public Category createCategory(Integer statusId, Integer typeId, List<CategoryTranslationDto> translations,
                                   Integer parentId) {
        CategoryType type = CategoryType.valueOf(typeId).orElseThrow(
                () -> new GenericServiceException("Invalid category type"));

        CategoryStatus status = CategoryStatus.valueOf(statusId).orElseThrow(
                () -> new GenericServiceException("Invalid category status"));

        Category parent = null;
        if (parentId != null) {
            parent = this.categoryRepository.findById(parentId).orElseThrow(
                    () -> new GenericServiceException(String.format("Category with id = %d does not exist", parentId)));
        }

        Category category = new Category();
        category.setType(type);
        category.setStatus(status);
        category.setParent(parent);

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
        category.setTranslations(categoryTranslations);

        try {
            this.categoryRepository.save(category);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return category;
    }

    public Category updateCategory(Integer categoryId, Integer statusId, Integer typeId,
                                   List<CategoryTranslationDto> translations, Integer parentId) {
        Category category = this.categoryRepository.findById(categoryId).orElseThrow(
                () -> new GenericServiceException(String.format("Category with id = %d does not exist", categoryId)));

        if (statusId != null) {
            CategoryStatus status = CategoryStatus.valueOf(statusId).orElseThrow(
                    () -> new GenericServiceException("Invalid category status"));
            category.setStatus(status);
        }

        if (typeId != null) {
            CategoryType type = CategoryType.valueOf(typeId).orElseThrow(
                    () -> new GenericServiceException("Invalid category type"));
            category.setType(type);
        }

        if (translations.size() > 0) {
            this.categoryTranslationRepository.deleteAll(category.getTranslations());

            Set<CategoryTranslation> categoryTranslations = new HashSet<>();
            for (CategoryTranslationDto translation: translations) {
                categoryTranslations.add(this.createCategoryTranslation(category,
                                                                        translation.getLanguageId(),
                                                                        translation.getTranslation()));
            }
            category.setTranslations(categoryTranslations);
        }

        if (parentId != null) {
            Category parent = this.categoryRepository.findById(parentId).orElseThrow(
                    () -> new GenericServiceException(String.format("Category with id = %d does not exist", parentId)));
            category.setParent(parent);
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

        try {
            if (this.entryRepository.CategoryExistsById(categoryId)) {
                category.setStatus(CategoryStatus.DELETED);
                this.categoryRepository.save(category);
            } else {
                this.categoryTranslationRepository.deleteAll(category.getTranslations());
                this.categoryRepository.delete(category);
            }
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
