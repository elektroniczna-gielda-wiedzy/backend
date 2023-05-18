package backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EntryDto {
    @JsonProperty("entry_type_id")
    Integer entryTypeId;
    @JsonProperty("title")
    String title;
    @JsonProperty("content")
    String content;
    @JsonProperty("categories")
    List<Integer> categories;
    @JsonProperty("image")
    String image;
}
