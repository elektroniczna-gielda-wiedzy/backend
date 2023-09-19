package backend.adapter.rest.model.common;

import backend.entry.model.CategoryTranslation;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryTranslationDto {
    @JsonProperty("lang_id")
    private Integer languageId;

    @JsonProperty("name")
    private String translatedName;

    public static CategoryTranslationDto buildFromModel(CategoryTranslation categoryTranslation) {
        return CategoryTranslationDto.builder()
                .languageId(categoryTranslation.getLanguage().getId())
                .translatedName(categoryTranslation.getTranslation())
                .build();
    }
}
