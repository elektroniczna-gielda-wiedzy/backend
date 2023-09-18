package backend.rest.security;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class JwtUtils {
    public static final String secretValue = "valueeeevalueeeevalueeeevalueeee";
    public static Key jwtKey = generateKey();

    public static Key generateKey() {
        return new SecretKeySpec(secretValue.getBytes(), "HmacSHA512");
    }
}
