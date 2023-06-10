package backend.services;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JwtServiceTest {
    @Test
    public void testJwtService() {
        JwtService service = new JwtService();
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("subject", 1);
        String token = service.generateToken(claimsMap);

        Map<String, Object> claimsDecoded = service.getClaims(token);
        Integer id = (Integer)claimsDecoded.get("subject");
        Assertions.assertThat(id).isEqualTo(1);
    }
}
