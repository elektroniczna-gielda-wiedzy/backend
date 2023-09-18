package backend.rest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    // TODO: Move to env vars.
    private static final String secret = "a9sdp98h293un9w8ehr80q7wr87sad8f8sdfasdoif9238j9joisdfpoijsadfpsoidjfpasodi";

    private static final Key key = new SecretKeySpec(secret.getBytes(), "HmacSHA512");

    private static final Integer expiration = 24 * 60 * 60 * 1000;  /* 24 hours */

    public String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(key)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
