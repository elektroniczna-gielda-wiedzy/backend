package backend.model.dto;

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
}
