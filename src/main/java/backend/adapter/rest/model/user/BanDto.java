package backend.adapter.rest.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BanDto {
    @JsonProperty("value")
    @NotNull(message = "value cannot be null")
    public Boolean value;
}
