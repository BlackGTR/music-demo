# Задание 6. HTTPS и CI — инструкция

## Что сделано

- **Цепочка сертификатов (3 звена):** Root CA → Intermediate CA → Server (все с `O=StudentID-23399`)
- **TLS/HTTPS:** профиль `https`, порт 8443, keystore `certs/musiclib-server.p12`
- **CI:** GitHub Actions (`.github/workflows/ci.yml`) и GitLab CI (`.gitlab-ci.yml`)
- **Секреты:** пароль keystore и JWT/DB — через переменные окружения (без утечек в коде)

---

## Секреты (GitHub Secrets / GitLab Variables)

### GitHub

Settings → Secrets and variables → Actions → New repository secret:

| Secret           | Описание                               |
|------------------|----------------------------------------|
| `DB_PASSWORD`    | Пароль PostgreSQL (для тестов в CI)    |
| `JWT_SECRET`     | Секрет для JWT                         |
| `SSL_KEYSTORE_PASSWORD` | Пароль keystore (опц., для деплоя с HTTPS) |

### GitLab

Settings → CI/CD → Variables → Add variable:

| Variable             | Описание                   | Protected / Masked |
|----------------------|----------------------------|---------------------|
| `DB_PASSWORD`        | Пароль PostgreSQL          | Masked              |
| `JWT_SECRET`         | Секрет JWT                 | Masked              |
| `SSL_KEYSTORE_PASSWORD` | Пароль keystore         | Masked              |

Для хранения самого keystore в CI (если нужно деплоить с HTTPS):

- Переменная `SSL_KEYSTORE_BASE64` — содержимое `certs/musiclib-server.p12` в Base64.
- Генерация (PowerShell):  
  `[Convert]::ToBase64String([IO.File]::ReadAllBytes("certs\musiclib-server.p12"))`
- **Keystore и пароль никогда не коммитятся** — добавлены в `.gitignore` (`.p12`, `.jks`).

---

## CI pipeline

1. **Compile** — сборка проекта
2. **Test** — тесты на H2 (без PostgreSQL в CI)
3. **Package** — создание `demo-1.0.0.jar`
4. **Upload artifact** — сохранение JAR в хранилище артефактов
