Music Library REST API. Проект на Spring Boot + PostgreSQL. Реализованы CRUD-операции и 5 бизнес-операций.

Тема проекта: REST-сервис музыкальной библиотеки. Основные сущности: пользователь, артист, альбом, трек, плейлист.

Сущности:
User – пользователь системы
Artist – исполнитель
Album – альбом исполнителя
Track – трек, принадлежащий артисту и альбому
Playlist – плейлист пользователя

REST-команды для проверки:

Пользователи:
POST /users/add?username=ИмяПользователя
GET /users

Артисты:
POST /artists/add?name=ИмяАртиста
GET /artists
GET /artists/id
GET /artists/{artistId}/albums

Альбомы:
POST /albums/add?title=Название&artistId=1
GET /albums

Треки:
POST /tracks/add?title=Название&duration=3:20&artistId=1&albumId=1
GET /tracks

Плейлисты:
POST /playlists/add?name=НазваниеПлейлиста&userId=1
POST /playlists/{playlistId}/addTrack?trackId=1
DELETE /playlists/{playlistId}/removeTrack/{trackId}
GET /playlists

Бизнес-операции (5 штук по заданию):

Создание плейлиста сразу со списком треков.
POST /playlists/create-with-tracks
JSON тело:
{
"name": "Workout",
"userId": 1,
"trackIds": [1, 2, 3]
}

Клонирование плейлиста другому пользователю:
POST /playlists/{playlistId}/clone?targetUserId=2&newName=НовоеИмя

Добавление всех треков альбома в плейлист:
POST /playlists/{playlistId}/add-album/{albumId}

Удаление трека из всех плейлистов:
DELETE /tracks/{trackId}/remove-from-all-playlists

Получение детальной информации о плейлисте (DTO без рекурсий):
GET /playlists/{playlistId}/details

База данных создаётся автоматически Hibernate. Таблицы: users, artists, albums, tracks, playlists, playlist_tracks.
