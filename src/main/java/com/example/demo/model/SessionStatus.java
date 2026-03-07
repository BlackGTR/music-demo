package com.example.demo.model;

/**
 * Статус сессии пользователя.
 * ACTIVE - сессия активна, refresh-токен можно использовать
 * REVOKED - сессия отозвана (например, после refresh или logout)
 * EXPIRED - сессия истекла по времени
 */
public enum SessionStatus {
    ACTIVE,
    REVOKED,
    EXPIRED
}
