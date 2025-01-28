package org.das.event_manager.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.das.event_manager.domain.User;
import org.das.event_manager.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenManager.class);
    private final SecretKeySpec secretKey;
    public final long expirationTime;

    public JwtTokenManager(
           @Value("${jwt.sign_key}") String secretKey,
           @Value("${jwt.live_time}") long expirationTime) {
        this.secretKey = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName()
        );
        this.expirationTime = expirationTime;
    }

    public String generateJwtToken(User user) {
        LOGGER.info("Generate jwt token for login = {}", user.login());
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.userRole().name());
        return Jwts
                .builder()
                .claims(claims)
                .subject(user.login())
                .signWith(secretKey)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .compact();
    }

    public String getLoginFromToken(String jwt) {
        LOGGER.info("Get login from jwt token = {} ", jwt);
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }


    public boolean isTokenValid(String jwt) {
        return false;
    }

    public String getRoleFromToken(String jwt) {
         LOGGER.info("Get role from jwt token = {} ", jwt);
        return Jwts
                .parser()
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .get("role", String.class);
    }
}
