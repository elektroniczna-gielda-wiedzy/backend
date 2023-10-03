package backend.adapter.rest.model.common;

import backend.entry.model.CategoryTranslation;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryTranslationDto {
    @JsonProperty("lang_id")
    @NotNull(message = "lang_id cannot be null")
    private Integer languageId;

    @JsonProperty("name")
    @NotNull(message = "name cannot be null")
    private String translatedName;

    public static CategoryTranslationDto buildFromModel(CategoryTranslation categoryTranslation) {
        return CategoryTranslationDto.builder()
                .languageId(categoryTranslation.getLanguage().getId())
                .translatedName(categoryTranslation.getTranslation())
                .build();
    }
}
