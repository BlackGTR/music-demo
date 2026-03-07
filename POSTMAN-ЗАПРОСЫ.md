# Postman — запросы для Music Library API

## Импорт коллекции

1. Откройте Postman
2. **File → Import** → выберите файл `Music-Library-API.postman_collection.json`
3. В коллекции есть переменные: `baseUrl`, `accessToken`, `refreshToken`

## Порядок выполнения

### 1. Регистрация или логин

Сначала выполните **Auth → Register** или **Auth → Login**:
- Register: `{"username": "testuser", "password": "TestPass1!"}`
- Login: те же данные (если пользователь уже создан)

Пароль должен содержать: минимум 8 символов, букву, цифру и спецсимвол.

Токены сохранятся автоматически в переменные коллекции (если включены Tests).

### 2. CRUD и бизнес-операции

Для всех запросов кроме Auth используется **Authorization: Bearer {{accessToken}}**.

Измените ID (1, 2, …) в URL на реальные ID из вашей БД.

---

## Папки коллекции

| Папка | Описание |
|-------|----------|
| **Auth** | Регистрация, логин, refresh |
| **Users (CRUD)** | Создание, чтение, изменение, удаление пользователей |
| **Artists (CRUD)** | CRUD артистов + альбомы артиста |
| **Albums (CRUD)** | CRUD альбомов |
| **Tracks (CRUD)** | Создание, чтение, изменение треков (DELETE /{id} отсутствует) |
| **Playlists (CRUD)** | CRUD плейлистов, добавление/удаление треков |
| **Бизнес-операции** | 5 операций (create-with-tracks, clone, add-album, remove-from-all, details) |
| **Сценарий JWT** | Последовательность: login → protected → refresh → old refresh (403) |

---

## Роли и доступ

| Роль | Tracks/Albums/Artists | Playlists | Users |
|------|------------------------|-----------|-------|
| ADMIN | GET, POST, PUT, DELETE | GET, POST, PUT, DELETE | все CRUD |
| USER | GET | GET, POST, PUT, DELETE | все CRUD |
| Не авторизован | — | — | — |

Auth-эндпоинты (register, login, refresh) доступны без токена.

---

## Базовый URL

По умолчанию: `http://localhost:8080`. Измените переменную `baseUrl` в коллекции, если порт другой.
