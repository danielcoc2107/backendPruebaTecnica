// src/main/java/com/acme/social/security/jwt/JwtService.java
package com.acme.social.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public final class JwtService {

    private final SecretKey key;

    public JwtService(String secret) {
        Objects.requireNonNull(secret);
        if (secret.length() < 32) throw new IllegalArgumentException("JWT secret debe tener >= 32 chars");
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String userIdUuid, String alias, String username, Duration ttl) {
        Instant now = Instant.now();
        Instant exp = now.plus(ttl);

        Map<String, Object> claims = Map.of(
                "alias", alias,
                "username", username
        );

        return Jwts.builder()
                .subject(userIdUuid)           // ✅ subject = UUID string
                .claims(claims)                // ✅ claims mínimos
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            parseAllClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractSubject(String token) {
        return parseAllClaims(token).getSubject();
    }

    public Claims extractClaims(String token) {
        return parseAllClaims(token);
    }

    private Claims parseAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
