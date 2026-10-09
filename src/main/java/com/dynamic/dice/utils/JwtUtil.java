package com.dynamic.dice.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    // Loaded from configuration (env var in prod) so the key is stable across
    // restarts and identical across every running instance.
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms:3600000}") // default 1 hour
    private long expirationMs;

    private Key key;

    @PostConstruct
    void init() {
        // Build a stable HMAC-SHA key from the configured Base64 secret (must be >= 32 bytes for HS256).
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(String userId, String username, String role, String adminId) {
        return Jwts.builder()
                .setSubject(userId)
                .addClaims(Map.of(
                        "username", username,
                        "role", role,
                        "adminId", adminId
                ))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
