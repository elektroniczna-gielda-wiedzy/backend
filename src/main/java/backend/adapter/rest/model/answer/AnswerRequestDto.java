package backend.adapter.rest.model.answer;

import backend.adapter.rest.model.common.ImageDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AnswerRequestDto {
    @JsonProperty("content")
    @NotNull(message = "content cannot be null")
    private String content;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("image")
    private ImageDto image;
}
