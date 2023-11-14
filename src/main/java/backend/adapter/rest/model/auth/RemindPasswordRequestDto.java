package backend.adapter.rest.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RemindPasswordRequestDto {
    @JsonProperty("email")
    @NotNull(message = "email cannot be null")
    private String email;
}
