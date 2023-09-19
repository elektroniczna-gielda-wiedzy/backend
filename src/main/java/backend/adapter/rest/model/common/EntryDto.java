package backend.adapter.rest.model.common;

import backend.entry.model.Entry;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.*;

@Getter
@Setter
@Builder
@Jacksonized
@AllArgsConstructor
public class EntryDto {
    @JsonProperty("entry_id")
    private Integer entryId;

    @JsonProperty("entry_type_id")
    @NotNull(message = "entry_type_id cannot be null")
    private Integer entryTypeId;

    @JsonProperty("title")
    @NotNull(message = "title cannot be null")
    private String title;

    @JsonProperty("author")
    private UserDto author;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("content")
    @NotNull(message = "content cannot be null")
    private String content;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("image")
    private String image;

    @JsonProperty("categories")
    @NotNull(message = "categories cannot be null")
    private List<CategoryDto> categories;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("favorite")
    private boolean favorite;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("answers")
    private List<AnswerDto> answers;

    public static EntryDto buildFromModel(Entry entry, boolean content, boolean answers) {
        EntryDtoBuilder builder = EntryDto.builder()
                .entryId(entry.getId())
                .entryTypeId(entry.getType().getId())
                .title(entry.getTitle())
                .author(UserDto.buildFromModel(entry.getAuthor()))
                .createdAt(entry.getCreatedAt())
                .categories(entry.getCategories().stream().map(CategoryDto::buildFromModel).toList());

        if (content) {
            builder.content(entry.getContent());
        }

        if (answers) {
            builder.answers(Optional.ofNullable(entry.getAnswers())
                                    .orElseGet(Collections::emptySet)
                                    .stream()
                                    .map(AnswerDto::buildFromModel)
                                    .toList());
        }

        return builder.build();
    }
}
