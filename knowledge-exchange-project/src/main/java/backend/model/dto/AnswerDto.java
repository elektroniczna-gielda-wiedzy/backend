package backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDto {
    @JsonProperty("content")
    private String content;
    @JsonProperty("image")
    private String image;

}
