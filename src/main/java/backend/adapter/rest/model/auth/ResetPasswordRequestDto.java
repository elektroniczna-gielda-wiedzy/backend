package backend.adapter.rest.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ResetPasswordRequestDto {
    @JsonProperty("old_password")
    @NotNull(message = "old password cannot be null")
    private String oldPassword;

    @JsonProperty("new_password")
    @NotNull(message = "new password cannot be null")
    private String newPassword;
}
