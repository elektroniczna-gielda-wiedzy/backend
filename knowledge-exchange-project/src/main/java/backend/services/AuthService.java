package backend.services;

import backend.model.StandardResponse;
import backend.model.dto.UserAuthDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

public class AuthService {

    private AuthenticationManager authManager;

    private JwtService jwtService;

    public AuthService(JwtService jwtService, AuthenticationManager authManager) {
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    public ResponseEntity<StandardResponse> login(UserAuthDto authDto) {
        return null;
    }


}
