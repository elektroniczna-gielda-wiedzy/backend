package backend.entry.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryTranslationDto {
    private Integer languageId;

    private String translation;

    public CategoryTranslationDto(Integer languageId, String translation) {
        this.languageId = languageId;
        this.translation = translation;
    }
}
