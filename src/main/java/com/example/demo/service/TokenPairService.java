package com.example.demo.service;

import com.example.demo.model.SessionStatus;
import com.example.demo.model.User;
import com.example.demo.model.UserSession;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserSessionRepository;
import com.example.demo.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

@Service
public class TokenPairService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;

    public TokenPairService(JwtTokenProvider jwtTokenProvider,
                            UserRepository userRepository,
                            UserSessionRepository userSessionRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
    }

    @Transactional
    public Map<String, String> createTokenPair(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername(), user.getRole());

        String jti = jwtTokenProvider.parseAndValidateRefreshToken(refreshToken).getBody().getId();
        Instant expiresAt = Instant.now().plusMillis(jwtTokenProvider.getRefreshValidityMs());

        UserSession session = new UserSession(user, jti, expiresAt);
        userSessionRepository.save(session);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    @Transactional
    public Map<String, String> refreshTokenPair(String refreshToken) {
        try {
            Claims claims = jwtTokenProvider.parseAndValidateRefreshToken(refreshToken).getBody();
            String jti = claims.getId();
            String username = claims.getSubject();
            String role = claims.get(JwtTokenProvider.CLAIM_ROLE, String.class);

            UserSession session = userSessionRepository.findByRefreshTokenJtiAndStatus(jti, SessionStatus.ACTIVE)
                    .orElseThrow(() -> new SecurityException("Session not found or revoked"));

            if (session.getExpiresAt().isBefore(Instant.now())) {
                userSessionRepository.updateStatusByRefreshTokenJti(jti, SessionStatus.EXPIRED);
                throw new SecurityException("Session expired");
            }

            userSessionRepository.updateStatusByRefreshTokenJti(jti, SessionStatus.REVOKED);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new SecurityException("User not found"));

            return createTokenPair(user);

        } catch (JwtException e) {
            throw new SecurityException("Invalid refresh token: " + e.getMessage());
        }
    }
}
