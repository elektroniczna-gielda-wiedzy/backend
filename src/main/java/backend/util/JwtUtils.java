package backend.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtUtils {
    public static Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
}
