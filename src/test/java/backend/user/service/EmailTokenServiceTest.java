package backend.user.service;

import backend.SpringContextRequiringTestBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import io.jsonwebtoken.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.TimeZone;

public class EmailTokenServiceTest extends SpringContextRequiringTestBase {
    @Autowired
    private EmailTokenService service;

    @Test
    public void correctTokenTest() {
        String token = service.generate(1);

        EmailTokenService service2 = new EmailTokenService(get3HourLaterClock());
        Assertions.assertThat(service2.verify(token)).isTrue();
        Assertions.assertThat(service2.getUserId(token)).isEqualTo(1);
    }

    @Test
    public void expiredTokenTest() {
        String token = service.generate(1);

        EmailTokenService service2 = new EmailTokenService(get5HourLaterClock());
        service2.verify(token);
        Assertions.assertThat(service2.verify(token)).isFalse();
    }

    private Clock get5HourLaterClock() {
        return () -> new Date(new Date().getTime() + 5 * 60 * 60 * 1000);
    }

    private Clock get3HourLaterClock() {
        return () -> new Date(new Date().getTime() + 3 * 60 * 60 * 1000);
    }


}
