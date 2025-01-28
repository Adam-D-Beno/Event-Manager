package org.das.event_manager.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.das.event_manager.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenManager.class);
    private final SecretKey SECRET_KEY;
    public final long EXPIRATION_TIME;

    public JwtTokenManager(
            @Value("${jwt.sign_key}") String secretKey,
            @Value("${jwt.live_time}") long expirationTime) {
        SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
        EXPIRATION_TIME = expirationTime;
    }

    public String generateJwtToken(String login) {
        LOGGER.info("Generate jwt token for login = {}", login);
        return Jwts
                .builder()
                .subject(login)
                .signWith(SECRET_KEY)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    public String getLoginFromToken(String jwt) {
        LOGGER.info("Get login from jwt token = {} ", jwt);
        return Jwts
                .parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }
}
