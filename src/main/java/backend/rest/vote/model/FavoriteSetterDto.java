package backend.rest.vote.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteSetterDto {
    @JsonProperty("value")
    @NotNull(message = "value cannot be null")
    public Integer value;
}
