package backend.adapter.rest.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    @JsonProperty("content")
    @NotNull(message = "content cannot be null")
    private String content;
}
