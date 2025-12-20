package com.example.demo.service;

import com.example.demo.dto.CreatePlaylistRequest;
import com.example.demo.dto.PlaylistDetailsDto;
import com.example.demo.model.Album;
import com.example.demo.model.Playlist;
import com.example.demo.model.Track;
import com.example.demo.model.User;
import com.example.demo.repository.AlbumRepository;
import com.example.demo.repository.PlaylistRepository;
import com.example.demo.repository.TrackRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;

    public PlaylistService(PlaylistRepository playlistRepository,
                           UserRepository userRepository,
                           TrackRepository trackRepository,
                           AlbumRepository albumRepository) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.trackRepository = trackRepository;
        this.albumRepository = albumRepository;
    }

    @Transactional
    public Playlist addPlaylist(String name, User user) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setUser(user);
        return playlistRepository.save(playlist);
    }

    @Transactional
    public Playlist addTrack(Long playlistId, Track track) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        playlist.getTracks().add(track);
        return playlistRepository.save(playlist);
    }

    public List<Playlist> getAll() {
        return playlistRepository.findAll();
    }

    public Playlist findById(Long id) {
        return playlistRepository.findById(id).orElse(null);
    }

    @Transactional
    public void removeTrack(Long playlistId, Long trackId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Track not found"));
        playlist.getTracks().remove(track);
        playlistRepository.save(playlist);
    }
    @Transactional
    public Playlist createPlaylistWithTracks(CreatePlaylistRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Playlist playlist = new Playlist();
        playlist.setName(request.getName());
        playlist.setUser(user);

        for (Long trackId : request.getTrackIds()) {
            Track track = trackRepository.findById(trackId)
                    .orElseThrow(() -> new IllegalArgumentException("Track not found: " + trackId));
            playlist.getTracks().add(track);
        }

        return playlistRepository.save(playlist);
    }
    private String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName(); // username из BasicAuth
    }
    private void assertOwnerOrAdmin(Playlist playlist) {
        String username = currentUsername();

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) return;

        if (playlist.getUser() == null || !username.equals(playlist.getUser().getUsername())) {
            throw new RuntimeException("Это не ваш плейлист!");
        }
    }
    // 2) Клонировать плейлист (можно другому пользователю, с другим именем)
    @Transactional
    public Playlist clonePlaylist(Long playlistId, Long targetUserId, String newName) {
        Playlist original = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        User owner = original.getUser();
        if (targetUserId != null) {
            owner = userRepository.findById(targetUserId)
                    .orElseThrow(() -> new IllegalArgumentException("Target user not found"));
        }

        Playlist copy = new Playlist();
        copy.setName(newName != null && !newName.isBlank()
                ? newName
                : original.getName() + " (copy)");
        copy.setUser(owner);

        // копируем треки
        copy.getTracks().addAll(original.getTracks());

        return playlistRepository.save(copy);
    }

    // 3) Добавить все треки альбома в плейлист
    @Transactional
    public Playlist addAlbumToPlaylist(Long playlistId, Long albumId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        List<Track> tracksFromAlbum = trackRepository.findByAlbum(album);
        for (Track track : tracksFromAlbum) {
            playlist.getTracks().add(track);
        }

        return playlistRepository.save(playlist);
    }

    // 4) Детали плейлиста
    public PlaylistDetailsDto getPlaylistDetails(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        PlaylistDetailsDto dto = new PlaylistDetailsDto();
        dto.setId(playlist.getId());
        dto.setName(playlist.getName());
        dto.setUsername(playlist.getUser().getUsername());

        List<Track> tracks = new ArrayList<>(playlist.getTracks());
        dto.setTrackCount(tracks.size());

        dto.setTrackTitles(
                tracks.stream().map(Track::getTitle).distinct().toList()
        );
        dto.setArtistNames(
                tracks.stream().map(t -> t.getArtist().getName()).distinct().toList()
        );
        dto.setAlbumTitles(
                tracks.stream().map(t -> t.getAlbum().getTitle()).distinct().toList()
        );

        return dto;
    }

    // 5) Удалить трек из всех плейлистов
    @Transactional
    public void removeTrackFromAllPlaylists(Long trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException("Track not found"));

        List<Playlist> allPlaylists = playlistRepository.findAll();
        for (Playlist playlist : allPlaylists) {
            if (playlist.getTracks().remove(track)) {
                playlistRepository.save(playlist);
            }
        }
    }
    public Playlist updateName(Long id, String name) {
        Playlist p = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
        assertOwnerOrAdmin(p);

        p.setName(name);
        return playlistRepository.save(p);
    }

    public void delete(Long playlistId) {
        playlistRepository.deleteById(playlistId);
    }
}
