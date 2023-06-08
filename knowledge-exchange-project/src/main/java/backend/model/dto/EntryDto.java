package backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@JsonDeserialize(builder = EntryDto.EntryDtoBuilder.class)
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
    private Timestamp createdAt;
    @JsonProperty("image")
    private String image;
    @JsonProperty("author")
    UserDto author;
    @JsonProperty("categories")
    private List<CategoryDto> categories;
}
