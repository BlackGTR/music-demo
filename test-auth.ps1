# PowerShell скрипт для проверки сценария аутентификации
# Использование: .\test-auth.ps1
# Убедитесь, что сервер запущен на http://localhost:8080

$baseUrl = "http://localhost:8080"

Write-Host "=== Шаг 1: POST /auth/login ===" -ForegroundColor Cyan
$loginBody = @{ username = "admin"; password = "admin" } | ConvertTo-Json
try {
    $loginResp = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    $accessToken = $loginResp.accessToken
    $refreshToken = $loginResp.refreshToken
    Write-Host "OK. accessToken и refreshToken получены." -ForegroundColor Green
} catch {
    Write-Host "Ошибка: $_" -ForegroundColor Red
    exit 1
}

Write-Host "`n=== Шаг 2: GET /tracks с access-токеном ===" -ForegroundColor Cyan
try {
    $tracksResp = Invoke-RestMethod -Uri "$baseUrl/tracks" -Method Get -Headers @{ Authorization = "Bearer $accessToken" }
    Write-Host "OK. Доступ есть." -ForegroundColor Green
} catch {
    Write-Host "Ошибка: $_" -ForegroundColor Red
}

Write-Host "`n=== Шаг 3: POST /auth/refresh (получить новую пару) ===" -ForegroundColor Cyan
$refreshBody = @{ refreshToken = $refreshToken } | ConvertTo-Json
try {
    $newPair = Invoke-RestMethod -Uri "$baseUrl/auth/refresh" -Method Post -Body $refreshBody -ContentType "application/json"
    $newRefreshToken = $newPair.refreshToken
    Write-Host "OK. Новая пара токенов получена." -ForegroundColor Green
} catch {
    Write-Host "Ошибка: $_" -ForegroundColor Red
}

Write-Host "`n=== Шаг 4: POST /auth/refresh со СТАРЫМ refresh-токеном ===" -ForegroundColor Cyan
$oldRefreshBody = @{ refreshToken = $refreshToken } | ConvertTo-Json
try {
    $badResp = Invoke-WebRequest -Uri "$baseUrl/auth/refresh" -Method Post -Body $oldRefreshBody -ContentType "application/json" -SkipHttpErrorCheck
    if ($badResp.StatusCode -eq 403) {
        Write-Host "OK. Ожидаемо 403 Forbidden. Старый refresh отозван." -ForegroundColor Green
    } else {
        Write-Host "Непредвиденно: статус $($badResp.StatusCode)" -ForegroundColor Yellow
    }
} catch {
    if ($_.Exception.Response.StatusCode -eq 403) {
        Write-Host "OK. Ожидаемо 403 Forbidden." -ForegroundColor Green
    } else {
        Write-Host "Ошибка: $_" -ForegroundColor Red
    }
}

Write-Host "`n=== Шаг 5: Проверьте таблицу user_sessions в БД ===" -ForegroundColor Cyan
Write-Host "SELECT id, user_id, refresh_token_jti, status, created_at FROM user_sessions;"
