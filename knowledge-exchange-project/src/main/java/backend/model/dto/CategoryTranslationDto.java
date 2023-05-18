package backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryTranslationDto {
    @JsonProperty("lang_id")
    private Integer languageId;
    @JsonProperty("name")
    private String categoryName;
}
