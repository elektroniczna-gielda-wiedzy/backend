package backend.rest.common.model;

import backend.model.dao.EntryDao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@Jacksonized
@AllArgsConstructor
public class EntryDto {
    @JsonProperty("entry_id")
    private Integer entryId;

    @JsonProperty("entry_type_id")
    private Integer entryTypeId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("author")
    private UserDto author;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("content")
    private String content;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private Date createdAt;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("image")
    private String image;

    @JsonProperty("categories")
    private List<CategoryDto> categories;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("favorite")
    private boolean favorite;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("answers")
    private List<AnswerDto> answers;

    public static EntryDto buildFromModel(EntryDao entry, boolean content, boolean answers) {
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
            builder.answers(entry.getAnswers().stream().map(AnswerDto::buildFromModel).toList());
        }

        return builder.build();
    }
}
