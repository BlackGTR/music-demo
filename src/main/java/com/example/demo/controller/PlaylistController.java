package com.example.demo.controller;

import com.example.demo.model.Playlist;
import com.example.demo.model.Track;
import com.example.demo.model.User;
import com.example.demo.service.PlaylistService;
import com.example.demo.service.TrackService;
import com.example.demo.service.UserService;
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
        playlistService.addTrack(playlistId, track);
        return playlistService.getAll().stream()
                .filter(p -> p.getId().equals(playlistId))
                .findFirst().orElseThrow();
    }

    @GetMapping
    public List<Playlist> getAll() {
        return playlistService.getAll();
    }
}
