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

    public List<Category> getCategories(String categoryStatus, Integer typeId, Integer parentId) {
        CategoryType type = null;
        if (typeId != null) {
            type = CategoryType.valueOf(typeId).orElseThrow(
                    () -> new GenericServiceException("Invalid category type"));
        }
        CategoryStatus status = CategoryStatus.valueOfLabel(categoryStatus).orElseThrow(
                () -> new GenericServiceException("Invalid category status"));

        List<Category> allCategories = this.categoryRepository.findAll(where(
                hasStatus(status).and(hasParent(parentId)).and(hasType(type))));

        return processCategoriesForConditions(allCategories, status);
    }

        private List<Category> processCategoriesForConditions(List<Category> allCategories, CategoryStatus status) {
            Set<Category> result = new HashSet<>(allCategories);
            for (Category category : allCategories) {
                if (category.getParentCategory() != null) {
                    if (status == CategoryStatus.ACTIVE) {
                        if (category.getParentCategory().getCategoryStatus() != CategoryStatus.ACTIVE) {
                            result.remove(category);
                        }
                    } else {
                        if (category.getParentCategory().getCategoryStatus() == CategoryStatus.ACTIVE) {
                            result.add(category.getParentCategory());
                        }
                    }
                }
            }
            return result.stream().toList();
        }


    public Category createCategory(String categoryStatus, Integer typeId, List<CategoryTranslationDto> translations,
                                   Integer parentId) {
        CategoryType type = CategoryType.valueOf(typeId).orElseThrow(
                () -> new GenericServiceException("Invalid category type"));
        CategoryStatus status = CategoryStatus.valueOfLabel(categoryStatus).orElseThrow(
                () -> new GenericServiceException("Invalid category status"));
        System.out.println(status);
        Category parent = null;
        if (parentId != null) {
            parent = this.categoryRepository.findById(parentId).orElseThrow(
                    () -> new GenericServiceException(String.format("Category with id = %d does not exist", parentId)));
        }

        Category category = new Category();
        category.setCategoryType(type);
        category.setCategoryStatus(status);
        category.setParentCategory(parent);

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
                                   Integer parentId, String categoryStatus) {
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

        if (categoryStatus != null) {
            CategoryStatus status = CategoryStatus.valueOfLabel(categoryStatus).orElseThrow(
                    () -> new GenericServiceException("Invalid category status"));
            category.setCategoryStatus(status);
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
                category.setCategoryStatus(CategoryStatus.DELETED);
                this.categoryRepository.save(category);
            } else {
                this.categoryTranslationRepository.deleteAll(category.getCategoryTranslations());
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
