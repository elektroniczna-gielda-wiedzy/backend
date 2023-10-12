package backend.adapter.rest.model.vote;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class BestAnswerDto {
    @JsonProperty("value")
    @Min(-1)
    @Max(1)
    @NotNull(message = "value cannot be null")
    Integer value;
}
