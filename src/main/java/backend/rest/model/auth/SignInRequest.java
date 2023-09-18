package backend.rest.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequest {
    @JsonProperty("email")
    @NotNull(message = "email cannot be null")
    private String email;

    @JsonProperty("password")
    @NotNull(message = "password cannot be null")
    private String password;

    @JsonProperty("remember_me")
    @NotNull(message = "remember_me cannot be null")
    private boolean rememberMeFlag;
}
