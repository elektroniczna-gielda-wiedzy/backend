package backend.adapter.rest.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ModifyPasswordRequestDto {
    @JsonProperty("token")
    @NotNull(message = "token cannot be null")
    private String token;

    @JsonProperty("new_password")
    @NotNull(message = "token cannot be null")
    private String newPassword;
}
