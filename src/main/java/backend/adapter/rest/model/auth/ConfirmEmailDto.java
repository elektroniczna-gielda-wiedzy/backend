package backend.adapter.rest.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
public class ConfirmEmailDto {
    @JsonProperty("token")
    @NotNull(message = "token cannot be null")
    private String token;
}
