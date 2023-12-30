package denshchikov.dmitry.app.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.jwt")
public class AppJwtProperties {

    @NotNull
    private SecretKey key;

    @NotEmpty
    private String issuer;

    @NotNull
    private JWSAlgorithm algorithm;

    @Min(1)
    private int expiresIn;

    public void setAlgorithm(String algorithm) {
        this.algorithm = JWSAlgorithm.parse(algorithm);
    }

    public void setKey(String key) {
        var jwk = new OctetSequenceKey.Builder(key.getBytes())
                .algorithm(algorithm)
                .build();

        this.key = jwk.toSecretKey();
    }

}
