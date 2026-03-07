package com.example.demo.repository;

import com.example.demo.model.SessionStatus;
import com.example.demo.model.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findByRefreshTokenJtiAndStatus(String refreshTokenJti, SessionStatus status);

    @Modifying
    @Query("UPDATE UserSession s SET s.status = :status WHERE s.refreshTokenJti = :jti")
    void updateStatusByRefreshTokenJti(@Param("jti") String jti, @Param("status") SessionStatus status);

    /** Помечает истекшие сессии как EXPIRED */
    @Modifying
    @Query("UPDATE UserSession s SET s.status = com.example.demo.model.SessionStatus.EXPIRED WHERE s.expiresAt < :now AND s.status = com.example.demo.model.SessionStatus.ACTIVE")
    int markExpiredSessions(@Param("now") Instant now);
}
