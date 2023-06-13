package backend.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    public static final String IMAGES_PATH = System.getProperty("user.dir").replace("\\", "/") + "/images/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String imgRepoPath = createImagesDirIfNotExists();
        registry.addResourceHandler("/**")
                .addResourceLocations("file:///" + imgRepoPath)
                .setCachePeriod(0);
    }

    private String createImagesDirIfNotExists() {
        try {
            Files.createDirectory(Path.of(IMAGES_PATH));
            return IMAGES_PATH;
        } catch (IOException e) {
            System.out.println("Failure during image dir creation. Image directory may already exist.");
        }
        return IMAGES_PATH;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:4200");
    }
}
