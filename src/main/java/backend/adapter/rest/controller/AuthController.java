package backend.adapter.rest.controller;

import backend.adapter.rest.model.auth.*;
import backend.user.model.AppUserDetails;
import backend.adapter.rest.Response;
import backend.adapter.rest.StandardBody;
import backend.common.service.GenericServiceException;
import backend.adapter.rest.security.JwtService;
import backend.user.model.User;
import backend.user.repository.UserRepository;
import backend.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;

    private final JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager,
                          JwtService jwtService, UserRepository userRepository) {
        this.userService = userService;
        this.authManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/sign_in", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> sign_in(@Valid @RequestBody SignInRequestDto request) {
        Authentication auth;

        try {
            auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                                                                                    request.getPassword()));
        } catch (AuthenticationException e) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.UNAUTHORIZED)
                    .addMessage("Invalid email or password")
                    .build();
        }

        AppUserDetails principal = (AppUserDetails) auth.getPrincipal();

        User user = userService.getUser(principal.getId());
        user.setLastLogin(Timestamp.from(Instant.now()));
        userRepository.save(user);

        if (!user.getIsEmailAuth()) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.UNAUTHORIZED)
                    .addMessage("Email address has not been confirmed")
                    .build();
        }

        SimpleGrantedAuthority grantedAuthority = (SimpleGrantedAuthority) auth.getAuthorities().toArray()[0];

        Map<String, Object> claims = new HashMap<>();
        claims.put("user", principal.getId());
        claims.put("role", grantedAuthority.getAuthority());
        String jwt = jwtService.generateToken(claims);

        return Response.builder()
                .httpStatusCode(HttpStatus.CREATED)
                .result(List.of(new SignInResultDto(jwt)))
                .build();
    }

    @PostMapping(path = "/sign_up", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> sign_up(@Valid @RequestBody SignUpRequestDto request) {
        try {
            this.userService.createUser(request.getEmail(),
                                        request.getPassword(),
                                        request.getFirstName(),
                                        request.getLastName(),
                                        false);
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

    @PutMapping(path = "/confirm_email", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> confirmEmail(@Valid @RequestBody ConfirmEmailDto confirmEmailDto) {
        try {
            this.userService.confirmEmail(confirmEmailDto.getToken());
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .build();
    }

    @PutMapping(path = "/resend_email", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> resendEmail(@Valid @RequestBody ResendEmailDto resendEmailDto) {
        try {
            this.userService.resendEmail(resendEmailDto.getEmail());
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .build();
    }

    @PutMapping(path = "/reset_password", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> resetPassword(@AuthenticationPrincipal AppUserDetails userDetails,
                                                      @Valid @RequestBody ResetPasswordRequestDto passwordRequestDto) {
        try {
            this.userService.resetPassword(userDetails.getId(), passwordRequestDto.getOldPassword(),
                                           passwordRequestDto.getNewPassword());
        } catch (Exception exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .build();
    }
}
