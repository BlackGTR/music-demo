package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    public static final String CLAIM_TYPE = "type";
    public static final String CLAIM_ROLE = "role";
    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";

    private static final long ACCESS_VALIDITY_MS = 15 * 60 * 1000L;        // 15 минут
    private static final long REFRESH_VALIDITY_MS = 7 * 24 * 60 * 60 * 1000L; // 7 дней

    private final SecretKey key;

    public JwtTokenProvider(@Value("${jwt.secret:default-secret-key-for-development-only-min-256-bits}") String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            keyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String username, String role) {
        String jti = UUID.randomUUID().toString();
        return buildToken(username, role, TOKEN_TYPE_ACCESS, jti, ACCESS_VALIDITY_MS);
    }

    public String generateRefreshToken(String username, String role) {
        String jti = UUID.randomUUID().toString();
        return buildToken(username, role, TOKEN_TYPE_REFRESH, jti, REFRESH_VALIDITY_MS);
    }

    private String buildToken(String subject, String role, String type, String jti, long validityMs) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .setId(jti)
                .addClaims(Map.of(
                        CLAIM_ROLE, role,
                        CLAIM_TYPE, type
                ))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + validityMs))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> parseAndValidateAccessToken(String token) {
        Jws<Claims> jws = parse(token);
        String type = jws.getBody().get(CLAIM_TYPE, String.class);
        if (!TOKEN_TYPE_ACCESS.equals(type)) {
            throw new JwtException("Token is not an access token");
        }
        return jws;
    }

    public Jws<Claims> parseAndValidateRefreshToken(String token) {
        Jws<Claims> jws = parse(token);
        String type = jws.getBody().get(CLAIM_TYPE, String.class);
        if (!TOKEN_TYPE_REFRESH.equals(type)) {
            throw new JwtException("Token is not a refresh token");
        }
        return jws;
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public String getJti(Claims claims) {
        return claims.getId();
    }

    public long getRefreshValidityMs() {
        return REFRESH_VALIDITY_MS;
    }
}
