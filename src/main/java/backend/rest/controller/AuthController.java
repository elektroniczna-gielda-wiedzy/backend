package backend.rest.controller;

import backend.model.AppUserDetails;
import backend.rest.model.auth.SignInRequest;
import backend.rest.model.auth.SignInResult;
import backend.rest.model.auth.SignUpRequest;
import backend.rest.Response;
import backend.rest.StandardBody;
import backend.service.GenericServiceException;
import backend.rest.security.JwtService;
import backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;

    private final AuthenticationManager authManager;

    private final JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping(path = "/sign_in", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> sign_in(@Valid @RequestBody
                                                SignInRequest request) {
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
        SimpleGrantedAuthority grantedAuthority = (SimpleGrantedAuthority) auth.getAuthorities().toArray()[0];

        Map<String, Object> claims = new HashMap<>();
        claims.put("user", principal.getId());
        claims.put("role", grantedAuthority.getAuthority());
        String jwt = jwtService.generateToken(claims);

        return Response.builder()
                .httpStatusCode(HttpStatus.CREATED)
                .result(List.of(new SignInResult(jwt)))
                .build();
    }

    @PostMapping(path = "/sign_up", consumes = MediaType.APPLICATION_JSON_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> sign_up(@Valid
                                                @RequestBody
                                                SignUpRequest request) {

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
}
