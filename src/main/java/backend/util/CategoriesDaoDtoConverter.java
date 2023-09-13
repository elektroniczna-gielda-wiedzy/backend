package backend.util;

import backend.model.dao.CategoryDao;
import backend.model.dto.CategoryDto;

public class CategoriesDaoDtoConverter {
    public static CategoryDto convertToDto(CategoryDao categoryDao) {
        return CategoryDto.builder()
                .categoryId(categoryDao.getId())
                .categoryType(categoryDao.getCategoryType().ordinal())
                .parentId(categoryDao.getParentCategory() != null ? categoryDao.getParentCategory()
                        .getId() : null)
                .names(categoryDao.getCategoryTranslations()
                               .stream()
                               .map(CategoryTranslationDaoDtoConverter::convertToDto)
                               .toList())
                .build();
    }
}
