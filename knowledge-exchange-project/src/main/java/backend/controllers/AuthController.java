package backend.controllers;

import backend.model.AppUserDetails;
import backend.model.StandardResponse;
import backend.model.dao.UserDao;
import backend.model.dto.AuthTokenDto;
import backend.model.dto.UserAuthDto;
import backend.model.dto.UserRegisterDto;
import backend.repositories.UserRepository;
import backend.services.AppUserDetailService;
import backend.services.JwtService;
import backend.util.RequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

    private PasswordEncoder encoder;

    private UserDetailsService userDetailsService;

    private AuthenticationManager authManager;
    private JwtService jwtService;
    private UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService, UserRepository userRepository, PasswordEncoder encoder) throws Exception {
        this.authManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @PostMapping("/sign_in")
    public ResponseEntity<StandardResponse> login(@RequestBody UserAuthDto authDto) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword())
            );
            Map<String, Object> claimsMap = new HashMap<>();
            AppUserDetails principal = (AppUserDetails) auth.getPrincipal();
            claimsMap.put("subject", principal.getId());
            SimpleGrantedAuthority grantedAuthority = (SimpleGrantedAuthority) auth.getAuthorities().toArray()[0];
            claimsMap.put("role", grantedAuthority.getAuthority());
            String jwt = jwtService.generateToken(claimsMap);
            return ResponseEntity.ok(
                StandardResponse.builder()
                    .success(true)
                    .messages(List.of())
                    .result(List.of(
                        AuthTokenDto.builder()
                            .sessionToken(jwt)
                            .build()
                    ))
                    .build()
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(
                    HttpStatus.UNAUTHORIZED
            ).body(StandardResponse.builder()
                    .success(false)
                    .messages(List.of("Bad email or password"))
                    .result(List.of())
                    .build());
        }
    }

    @PostMapping("/sign_up")
    public ResponseEntity<StandardResponse> register(@RequestBody UserRegisterDto registerDto) {
        List<String> errors = RequestValidator.validateRegister(registerDto);
        try {
            if (errors.size() > 0) {
                throw new RuntimeException("Request validation error");
            }
            UserDao userDao = userRepository.findUserDaoByEmail(registerDto.getEmail());
            if(userDao != null) {
                errors.add("User with given email exists");
                throw new RuntimeException("User with given email exists");
            }
            UserDao dao = new UserDao();
            dao.setEmail(registerDto.getEmail());
            dao.setPassword(encoder.encode(registerDto.getPassword()));
            dao.setFirstName(registerDto.getFirstName());
            dao.setLastName(registerDto.getLastName());
            //TODO temporary
            dao.setIsEmailAuth(true);
            dao.setIsActive(true);
            dao.setIsAdmin(false);
            dao.setCreatedAt(Timestamp.from(Instant.now()));
            userRepository.save(dao);
            return ResponseEntity.ok()
                .body(
                    StandardResponse.builder()
                        .success(true)
                        .messages(errors)
                        .result(List.of())
                        .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(
                    HttpStatus.BAD_REQUEST
            ).body(
                StandardResponse.builder()
                    .success(false)
                    .messages(errors)
                    .result(List.of())
                    .build()
            );
        }

    }


}
