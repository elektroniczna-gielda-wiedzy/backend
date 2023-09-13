package backend.util;

import backend.model.dao.CategoryTranslation;
import backend.model.dto.CategoryTranslationDto;

public class CategoryTranslationDaoDtoConverter {
    public static CategoryTranslationDto convertToDto(CategoryTranslation categoryTranslation) {
        return CategoryTranslationDto.builder()
                .languageId(categoryTranslation.getLanguage().getId())
                .translatedName(categoryTranslation.getTranslation())
                .build();
    }
}
