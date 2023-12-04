package backend.adapter.rest.security;

import backend.user.service.EmailTokenService;
import io.jsonwebtoken.Clock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

public class JwtServiceTest {

    JwtService service = new JwtService(getCurrentClock());

    @Test
    public void correctTokenTest() {
        String token = service.generateToken(Map.of("user_id", 1));

        JwtService service2 = new JwtService(get23HourLaterClock());
        Assertions.assertThat(service2.isValid(token)).isTrue();
        Assertions.assertThat(service2.getClaims(token).get("user_id")).isEqualTo(1.0);
    }

    @Test
    public void expiredTokenTest() {
        String token = service.generateToken(Map.of("user_id", 1));

        JwtService service2 = new JwtService(get25HourLaterClock());
        Assertions.assertThat(service2.isValid(token)).isFalse();
    }

    private Clock get25HourLaterClock() {
        return () -> new Date(new Date().getTime() + 25 * 60 * 60 * 1000);
    }

    private Clock get23HourLaterClock() {
        return () -> new Date(new Date().getTime() + 23 * 60 * 60 * 1000);
    }

    private Clock getCurrentClock() {
        return Date::new;
    }

}
