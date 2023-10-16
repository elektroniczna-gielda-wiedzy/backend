package backend.adapter.rest.model.entry;

import backend.adapter.rest.model.common.CategoryDto;
import backend.adapter.rest.model.common.ImageDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class EntryRequestDto {
    @JsonProperty("entry_type_id")
    @NotNull(message = "entry_type_id cannot be null")
    private Integer entryTypeId;

    @JsonProperty("title")
    @NotNull(message = "title cannot be null")
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("content")
    @NotNull(message = "content cannot be null")
    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("image")
    private ImageDto image;

    @JsonProperty("categories")
    @NotNull(message = "categories cannot be null")
    private List<CategoryDto> categories;
}
