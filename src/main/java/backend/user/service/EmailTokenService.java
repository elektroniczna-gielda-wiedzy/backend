package backend.user.service;

import backend.common.service.GenericServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailTokenService {
    // TODO: Move to env vars.
    private static final String secret = "oainsd98h129nadnp9a8h913jdsunfisdkasndffisdjoifasdfp9283hoianso12casxapljut";

    private static final Key key = new SecretKeySpec(secret.getBytes(), "HmacSHA512");

    private static final Integer expiration = 4 * 60 * 60 * 1000;  /* 4 hours */

    public String generate(Integer userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", userId);

        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration))
                .signWith(key)
                .compact();
    }

    public boolean verify(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Integer getUserId(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return ((Double) claims.get("user")).intValue();
        } catch (Exception exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }
}
