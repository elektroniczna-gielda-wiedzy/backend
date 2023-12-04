package backend.user.service;

import backend.SpringContextRequiringTestBase;
import io.jsonwebtoken.Clock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PasswordReminderTokenServiceTest extends SpringContextRequiringTestBase {
    @Autowired
    private PasswordReminderTokenService service;

    @Test
    public void correctTokenTest() {
        String token = service.generate(1);

        PasswordReminderTokenService service2 = new PasswordReminderTokenService(get23HourLaterClock());
        service2.verify(token);
        Assertions.assertThat(service2.verify(token)).isTrue();
        Assertions.assertThat(service2.getUserId(token)).isEqualTo(1);
    }

    @Test
    public void expiredTokenTest() {
        String token = service.generate(1);

        PasswordReminderTokenService service2 = new PasswordReminderTokenService(get25HourLaterClock());
        service2.verify(token);
        Assertions.assertThat(service2.verify(token)).isFalse();
    }

    private Clock get25HourLaterClock() {
        return () -> new Date(new Date().getTime() + 25 * 60 * 60 * 1000);
    }

    private Clock get23HourLaterClock() {
        return () -> new Date(new Date().getTime() + 23 * 60 * 60 * 1000);
    }
}
