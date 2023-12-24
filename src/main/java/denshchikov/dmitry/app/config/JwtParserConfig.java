package denshchikov.dmitry.app.config;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtParserConfig {

    private final AppJwtProperties appJwtProperties;

    @Bean
    public JwtParser jwtParser() {
        var key = appJwtProperties.getKey();

        return Jwts.parser()
                .verifyWith(key)
                .build();
    }

}
