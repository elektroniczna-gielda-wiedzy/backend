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
    private Integer categoryType;

    @JsonProperty("names")
    @NotNull(message = "names cannot be null")
    private List<CategoryTranslationDto> names;

    @JsonProperty("parent_id")
    @NotNull(message = "parent_id cannot be null")
    private Integer parentId;

    public CategoryDto(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public CategoryDto(Integer categoryId, Integer categoryType, List<CategoryTranslationDto> names, Integer parentId) {
        this.categoryId = categoryId;
        this.categoryType = categoryType;
        this.names = names;
        this.parentId = parentId;
    }

    public static CategoryDto buildFromModel(Category category) {
        return CategoryDto.builder()
                .categoryId(category.getId())
                .categoryType(category.getCategoryType().ordinal())
                .names(category.getCategoryTranslations().stream().map(CategoryTranslationDto::buildFromModel).toList())
                .parentId(category.getParentCategory() != null ? category.getParentCategory().getId() : null)
                .build();
    }
}
