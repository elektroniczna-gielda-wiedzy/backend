package backend.rest.auth.signin;

import backend.model.AppUserDetails;
import backend.rest.common.Response;
import backend.rest.common.StandardBody;
import backend.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/auth/sign_in")
public class SignInController {
    private final AuthenticationManager authManager;

    private final JwtService jwtService;

    public SignInController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> sign_in(@Valid @RequestBody SignInRequest request) {
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
}
