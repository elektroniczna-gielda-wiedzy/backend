package backend.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.SimpleDateFormat;

@Configuration
public class SerializerConfig {
    @Bean
    ObjectMapper objectMapper() {
        Jackson2ObjectMapperBuilder mapperBuilder = new Jackson2ObjectMapperBuilder();
        mapperBuilder.dateFormat(new SimpleDateFormat("dd:MM:yyyy HH:mm"));
        return mapperBuilder.build();
    }
}
