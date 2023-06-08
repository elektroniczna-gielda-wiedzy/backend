package backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CategoryDto {

    public CategoryDto(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public CategoryDto(Integer categoryId,
                       Integer categoryType,
                       List<CategoryTranslationDto> names,
                       Integer parentId) {
        this.categoryId = categoryId;
        this.categoryType = categoryType;
        this.names = names;
        this.parentId = parentId;
    }

    @JsonProperty("category_id")
    private Integer categoryId;
    @JsonProperty("type")
    private Integer categoryType;
    @JsonProperty("names")
    private List<CategoryTranslationDto> names;
    @JsonProperty("parent_id")
    private Integer parentId;
}
