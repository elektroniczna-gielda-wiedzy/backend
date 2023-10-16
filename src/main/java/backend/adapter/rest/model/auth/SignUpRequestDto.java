package backend.adapter.rest.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
    @JsonProperty("email")
    @NotNull(message = "email cannot be null")
    @Email(regexp = "[a-zA-Z0-9_.+-]+@student.agh.edu.pl")
    private String email;

    @JsonProperty("password")
    @NotNull(message = "password cannot be null")
    private String password;

    @JsonProperty("first_name")
    @NotNull(message = "first_name cannot be null")
    private String firstName;

    @JsonProperty("last_name")
    @NotNull(message = "last_name cannot be null")
    private String lastName;
}
