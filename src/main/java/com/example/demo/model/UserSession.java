package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user_sessions", indexes = {
        @Index(name = "idx_user_sessions_refresh_jti", unique = true, columnList = "refreshTokenJti"),
        @Index(name = "idx_user_sessions_user_id", columnList = "user_id")
})
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Уникальный идентификатор refresh-токена (claim jti) */
    @Column(name = "refresh_token_jti", nullable = false, unique = true, length = 64)
    private String refreshTokenJti;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionStatus status = SessionStatus.ACTIVE;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant expiresAt;

    public UserSession() {}

    public UserSession(User user, String refreshTokenJti, Instant expiresAt) {
        this.user = user;
        this.refreshTokenJti = refreshTokenJti;
        this.expiresAt = expiresAt;
    }
}
