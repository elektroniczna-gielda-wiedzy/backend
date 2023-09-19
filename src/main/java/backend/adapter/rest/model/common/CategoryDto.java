package backend.adapter.rest.model.common;

import backend.entry.model.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Integer categoryType;

    @JsonProperty("names")
    private List<CategoryTranslationDto> names;

    @JsonProperty("parent_id")
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
