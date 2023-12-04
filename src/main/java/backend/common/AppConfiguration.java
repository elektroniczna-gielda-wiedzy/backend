package backend.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.Clock;

import java.util.Date;

@Configuration
public class AppConfiguration {
    @Bean
    public Clock getClock() {
        return Date::new;
    }
}
