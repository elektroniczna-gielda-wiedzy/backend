package backend.entry.service;

import backend.SpringContextRequiringTestBase;
import backend.entry.model.*;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CategoryServiceIntegrationTest extends SpringContextRequiringTestBase {

    @Autowired
    CategoryService categoryService;


    @Test
    @Order(1)
    public void testCreatingCategory() {
        List<Category> categories = categoryService.getCategories(CategoryStatus.ACTIVE.getId(),
                                                                  CategoryType.DEPARTMENT.getId(), 1);

        List<CategoryTranslationDto> translations = new ArrayList<>();
        translations.add(CategoryTranslationDto.builder()
                                 .languageId(1)
                                 .translation("kategoria")
                                 .build());
        translations.add(CategoryTranslationDto.builder()
                                 .languageId(2)
                                 .translation("category")
                                 .build());
        Category category = categoryService.createCategory(CategoryStatus.ACTIVE.getId(),
                                                          CategoryType.DEPARTMENT.getId(),
                                       translations, 1);

        List<Category> categoriesAfterAdd = categoryService.getCategories(CategoryStatus.ACTIVE.getId(),
                                                                          CategoryType.DEPARTMENT.getId(), 1);

        Assertions.assertThat(categoriesAfterAdd.size()).isEqualTo(categories.size() + 1);
    }


    @Test
    @Transactional
    @Order(2)
    public void testEditingCategory() {

        List<Category> categories = categoryService.getCategories(CategoryStatus.ACTIVE.getId(),
                                                                  CategoryType.DEPARTMENT.getId(), 1);
        List<CategoryTranslationDto> translations = new ArrayList<>();
        translations.add(CategoryTranslationDto.builder()
                                 .languageId(1)
                                 .translation("kategoria")
                                 .build());
        translations.add(CategoryTranslationDto.builder()
                                 .languageId(2)
                                 .translation("categoryEdited")
                                 .build());

        Category categoryToEdit =
                categories.stream().filter((category) -> category.getTranslations().stream().map(CategoryTranslation::getTranslation).anyMatch(
                        "kategoria"::equals)).toList().get(0);

        categoryService.updateCategory(categoryToEdit.getId(), CategoryStatus.ACTIVE.getId(), CategoryType.DEPARTMENT.getId(),
                                       translations, 1);

        List<Category> categoriesAfterEdit = categoryService.getCategories(CategoryStatus.ACTIVE.getId(),
                                                                             CategoryType.DEPARTMENT.getId(), 1);

        Assertions.assertThat(categoriesAfterEdit.stream().flatMap((category) -> category.getTranslations().stream())
                                      .map(CategoryTranslation::getTranslation)
                                      .toList()).contains("categoryEdited");
    }

    @Test
    @Transactional
    @Order(3)
    public void testDeletingCategory() {
        List<Category> categories = categoryService.getCategories(CategoryStatus.ACTIVE.getId(),
                                                                  CategoryType.DEPARTMENT.getId(), 1);

        Category categoryToDelete =
                categories.stream().filter((category) -> category.getTranslations().stream().map(CategoryTranslation::getTranslation).anyMatch(
                        "kategoria"::equals)).toList().get(0);

        System.out.println();

        categoryService.deleteCategory(categoryToDelete.getId());

        List<Category> categoriesAfterDelete = categoryService.getCategories(CategoryStatus.ACTIVE.getId(),
                                                                  CategoryType.DEPARTMENT.getId(), 1);

        Assertions.assertThat(categoriesAfterDelete.size()).isEqualTo(categories.size() - 1);
    }


}
