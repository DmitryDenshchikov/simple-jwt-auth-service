package denshchikov.dmitry.simpleeventmanagementservice.service;

import denshchikov.dmitry.simpleeventmanagementservice.config.AppJwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final JwtParser jwtParser;
    private final AppJwtProperties appJwtProperties;

    public Claims getClaims(String jwt) {
        return jwtParser.parseSignedClaims(jwt).getPayload();
    }

    public String generateJWT(Map<String, Object> claims) {
        var issuer = appJwtProperties.getIssuer();
        var key = appJwtProperties.getKey();
        var issuedAt = Instant.now();
        var expiration = issuedAt.plusMillis(appJwtProperties.getExpiresIn());

        return Jwts.builder()
                .issuer(issuer)
                .claims(claims)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiration))
                .signWith(key)
                .compact();
    }

}
