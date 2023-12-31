package denshchikov.dmitry.app;

import denshchikov.dmitry.app.config.AppJwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppJwtProperties.class)
public class SimpleJwtAuthService {

    public static void main(String[] args) {
        SpringApplication.run(SimpleJwtAuthService.class, args);
    }

}
