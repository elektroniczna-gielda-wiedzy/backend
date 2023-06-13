package backend.model.dto;

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
    @JsonProperty("content")
    private String content;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("image")
    private String image;
    @JsonProperty("author")
    UserDto author;
    @JsonProperty("categories")
    private List<CategoryDto> categories;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("favorite")
    private boolean favorite;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("answers")
    private List<AnswerDto> answersList;

}
