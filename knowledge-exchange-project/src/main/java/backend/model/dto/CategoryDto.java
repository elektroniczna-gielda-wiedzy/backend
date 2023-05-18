package backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryDto {
    @JsonProperty("type")
    Integer categoryType;
    @JsonProperty("names")
    List<CategoryTranslationDto> categoryTranslationDtoList;
    @JsonProperty("parent_id")
    Integer parentId;
}
