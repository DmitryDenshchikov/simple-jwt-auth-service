package denshchikov.dmitry.app.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.jwt")
public class AppJwtProperties {

    @NotEmpty
    private String secret;

    @NotEmpty
    private String issuer;

    @NotEmpty
    private String algorithm;

    @Min(1)
    private int expiresIn;

    public SecretKey getKey() {
        return new SecretKeySpec(secret.getBytes(), algorithm);
    }

}
