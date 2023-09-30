package backend.adapter.rest.security;

import backend.user.model.AppUserDetails;
import backend.user.model.User;
import backend.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;

    public JwtFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        JwtService jwtService = new JwtService();
        if (token != null && jwtService.isValid(token)) {
            Claims userClaims = jwtService.getClaims(token);
            Integer userId = ((Double) userClaims.get("user")).intValue();
            String role = (String) userClaims.get("role");
            List<GrantedAuthority> grantedAuthorityList = List.of(new SimpleGrantedAuthority(role));
            User user = this.userRepository.findById(userId).get();
            AppUserDetails userDetails = new AppUserDetails(user);
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, grantedAuthorityList);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
