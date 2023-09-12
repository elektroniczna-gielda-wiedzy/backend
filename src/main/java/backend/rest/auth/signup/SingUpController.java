package backend.rest.auth.signup;

import backend.rest.common.Response;
import backend.rest.common.StandardBody;
import backend.services.GenericServiceException;
import backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/auth/sign_up")
public class SingUpController {
    private final UserService userService;

    public SingUpController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> sign_up(@Valid @RequestBody SignUpRequest request) {

        try {
            this.userService.register(request.getEmail(),
                                      request.getPassword(),
                                      request.getFirstName(),
                                      request.getLastName());
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.CREATED)
                .build();
    }
}
