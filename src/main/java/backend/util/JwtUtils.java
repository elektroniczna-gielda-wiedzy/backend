package backend.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class JwtUtils {
    public static final String secretValue = "valueeeevalueeeevalueeeevalueeee";
    public static Key jwtKey = generateKey();

    public static Key generateKey() {
        return new SecretKeySpec(secretValue.getBytes(), "HmacSHA512");
    }
}
