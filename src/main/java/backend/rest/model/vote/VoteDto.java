package backend.rest.model.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDto {
    @JsonProperty("value")
    @NotNull(message = "value cannot be null")
    Integer value;
}
