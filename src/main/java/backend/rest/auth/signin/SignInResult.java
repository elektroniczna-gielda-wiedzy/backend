package backend.rest.auth.signin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
@AllArgsConstructor
public class SignInResult {
    @JsonProperty("session_token")
    private String sessionToken;
}
