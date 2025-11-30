package com.example.demo.controller;

import com.example.demo.model.Playlist;
import com.example.demo.model.Track;
import com.example.demo.model.User;
import com.example.demo.service.PlaylistService;
import com.example.demo.service.TrackService;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.CreatePlaylistRequest;
import com.example.demo.dto.PlaylistDetailsDto;
import com.example.demo.model.Playlist;
import com.example.demo.service.PlaylistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final UserService userService;
    private final TrackService trackService;

    public PlaylistController(PlaylistService playlistService, UserService userService, TrackService trackService) {
        this.playlistService = playlistService;
        this.userService = userService;
        this.trackService = trackService;

    }

    @PostMapping("/add")
    public Playlist addPlaylist(@RequestParam String name, @RequestParam Long userId) {
        User user = userService.findById(userId);
        if (user == null) throw new RuntimeException("User not found");
        return playlistService.addPlaylist(name, user);
    }

    @PostMapping("/{playlistId}/addTrack")
    public Playlist addTrackToPlaylist(@PathVariable Long playlistId, @RequestParam Long trackId) {
        Track track = trackService.findById(trackId);
        if (track == null) throw new RuntimeException("Track not found");
        return playlistService.addTrack(playlistId, track);
    }

    @DeleteMapping("/{playlistId}/removeTrack/{trackId}")
    public void removeTrackFromPlaylist(@PathVariable Long playlistId,
                                        @PathVariable Long trackId) {
        playlistService.removeTrack(playlistId, trackId);
    }

    @GetMapping
    public List<Playlist> getAll() {
        return playlistService.getAll();
    }

    @GetMapping("/{id}")
    public Playlist getById(@PathVariable Long id) {
        return playlistService.findById(id);
    }
    @PostMapping("/create-with-tracks")
    public Playlist createWithTracks(@RequestBody CreatePlaylistRequest request) {
        return playlistService.createPlaylistWithTracks(request);
    }

    // 2) Клонировать плейлист
    @PostMapping("/{playlistId}/clone")
    public Playlist clonePlaylist(
            @PathVariable Long playlistId,
            @RequestParam(required = false) Long targetUserId,
            @RequestParam(required = false) String newName
    ) {
        return playlistService.clonePlaylist(playlistId, targetUserId, newName);
    }

    // 3) Добавить все треки альбома в плейлист
    @PostMapping("/{playlistId}/add-album/{albumId}")
    public Playlist addAlbumToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long albumId
    ) {
        return playlistService.addAlbumToPlaylist(playlistId, albumId);
    }

    // 4) Детали плейлиста
    @GetMapping("/{playlistId}/details")
    public PlaylistDetailsDto getDetails(@PathVariable Long playlistId) {
        return playlistService.getPlaylistDetails(playlistId);
    }
    @PutMapping("/{id}")
    public Playlist update(@PathVariable Long id,
                           @RequestParam(required = false) String name,
                           @RequestParam(required = false) Long userId) {
        return playlistService.update(id, name, userId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        playlistService.delete(id);
    }
}
