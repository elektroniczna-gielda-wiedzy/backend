package backend.adapter.rest.model.common;

import backend.entry.model.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CategoryDto {
    @JsonProperty("category_id")
    private Integer categoryId;

    @JsonProperty("type")
    @NotNull(message = "type cannot be null")
    private Integer type;

    @JsonProperty("names")
    @NotNull(message = "names cannot be null")
    private List<CategoryTranslationDto> names;

    @JsonProperty("parent_id")
    private Integer parentId;

    @JsonProperty("status")
    private Integer status;

    public CategoryDto(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public CategoryDto(Integer categoryId, Integer type, List<CategoryTranslationDto> names,
                       Integer parentId, Integer status) {
        this.categoryId = categoryId;
        this.type = type;
        this.names = names;
        this.parentId = parentId;
        this.status = status;
    }

    public static CategoryDto buildFromModel(Category category) {
        return CategoryDto.builder()
                .categoryId(category.getId())
                .type(category.getType().ordinal())
                .names(category.getTranslations().stream().map(CategoryTranslationDto::buildFromModel).toList())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .status(category.getStatus().getId())
                .build();
    }
}
