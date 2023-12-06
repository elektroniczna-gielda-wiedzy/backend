package backend.user.service;

import backend.common.service.GenericServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import io.jsonwebtoken.Clock;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PasswordReminderTokenService {

    private static final String secret = "jpvtpgp99hsbpjyk6vnnfwgysgdj1cpwsgzptd9nrezhr737qklm4m65ibfl1th2jizfk2dg0in";

    private static final Key key = new SecretKeySpec(secret.getBytes(), "HmacSHA512");

    private static final Integer expiration = 24 * 60 * 60 * 1000;

    private Clock clock;

    public PasswordReminderTokenService(Clock clock) {
        this.clock = clock;
    }

    public String generate(Integer userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", userId);

        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(clock.now())
                .setExpiration(new Date(clock.now().getTime() + expiration))
                .signWith(key)
                .compact();
    }

    public boolean verify(String token) {
        try {
            Jwts.parserBuilder().setClock(clock).setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Integer getUserId(String token) {
        try {
            Claims claims =
                    Jwts.parserBuilder().setClock(clock).setSigningKey(key).build().parseClaimsJws(token).getBody();
            return ((Double) claims.get("user")).intValue();
        } catch (Exception exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }
}
