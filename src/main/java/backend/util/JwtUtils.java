package backend.util;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class JwtUtils {
    public static final String secretValue = "valueeeevalueeeevalueeeevalueeee";

    public static final String secretConfirmationValue = "valueee2valueee2valueee2valueee2";

    public static Key jwtKey = generateKey();

    public static Key generateKey() {
        return new SecretKeySpec(secretValue.getBytes(), "HmacSHA512");
    }

    public static Key generateEmailConfirmationKey() {
        return new SecretKeySpec(secretConfirmationValue.getBytes(), "HmacSHA512");
    }
}
