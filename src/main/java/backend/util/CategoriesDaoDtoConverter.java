package backend.util;

import backend.model.dao.Category;
import backend.model.dto.CategoryDto;

public class CategoriesDaoDtoConverter {
    public static CategoryDto convertToDto(Category category) {
        return CategoryDto.builder()
                .categoryId(category.getId())
                .categoryType(category.getCategoryType().ordinal())
                .parentId(category.getParentCategory() != null ? category.getParentCategory()
                        .getId() : null)
                .names(category.getCategoryTranslations()
                               .stream()
                               .map(CategoryTranslationDaoDtoConverter::convertToDto)
                               .toList())
                .build();
    }
}
