package backend.adapter.rest.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
public class ResendEmailDto {
    @JsonProperty("email")
    @NotNull(message = "email cannot be null")
    private String email;
}
