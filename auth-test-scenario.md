# Сценарий тестирования аутентификации (access + refresh токены)

## Предварительные условия
- Сервер запущен: `mvn spring-boot:run`
- PostgreSQL доступна (musicdb)
- Есть пользователь, например `admin` / `admin` (создайте через UserService/UserController при необходимости)

---

## Шаг 1. POST /auth/login — получить access и refresh

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"admin\"}"
```

**Ожидаемый ответ:**
```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc..."
}
```

Сохраните оба токена для следующих шагов.

---

## Шаг 2. Доступ к защищённому эндпоинту с access-токеном

```bash
curl -X GET http://localhost:8080/tracks \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

**Ожидаемый результат:** HTTP 200, список треков (или пустой массив).

---

## Шаг 3. POST /auth/refresh — получить новую пару токенов

```bash
curl -X POST http://localhost:8080/auth/refresh \
  -H "Content-Type: application/json" \
  -d "{\"refreshToken\":\"YOUR_REFRESH_TOKEN\"}"
```

**Ожидаемый ответ:**
```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc..."
}
```

Сохраните **новые** токены. **Старый** refresh-токен после этого становится недействительным (сессия помечена как REVOKED).

---

## Шаг 4. Повторный вызов /auth/refresh со старым refresh-токеном

Используйте **старый** refresh-токен (из шага 1), а не новый.

```bash
curl -X POST http://localhost:8080/auth/refresh \
  -H "Content-Type: application/json" \
  -d "{\"refreshToken\":\"OLD_REFRESH_TOKEN\"}"
```

**Ожидаемый результат:** HTTP 403 (Forbidden), тело вида:
```json
{
  "error": "Session not found or revoked"
}
```

---

## Шаг 5. Проверка статусов сессий в БД

```sql
SELECT id, user_id, refresh_token_jti, status, created_at, expires_at 
FROM user_sessions 
ORDER BY id DESC;
```

**Ожидаемо:**
- Сессия от шага 1 — `status = 'REVOKED'` (после refresh)
- Сессия от шага 3 — `status = 'ACTIVE'`
- При истечении срока `expires_at` — можно помечать как `EXPIRED`

---

## Краткая сводка

| Шаг | Эндпоинт | Действие | Ожидание |
|-----|----------|----------|----------|
| 1 | POST /auth/login | Логин | 200, accessToken + refreshToken |
| 2 | GET /tracks | Запрос с access | 200, данные |
| 3 | POST /auth/refresh | Обмен refresh на новую пару | 200, новая пара токенов |
| 4 | POST /auth/refresh | Повтор с **старым** refresh | 403, error |
| 5 | SQL | Проверка user_sessions | Корректные status (ACTIVE, REVOKED) |
