package com.example.demo.controller;

import com.example.demo.model.Album;
import com.example.demo.model.Artist;
import com.example.demo.model.Track;
import com.example.demo.service.AlbumService;
import com.example.demo.service.ArtistService;
import com.example.demo.service.TrackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;
    private final ArtistService artistService;
    private final AlbumService albumService;

    public TrackController(TrackService trackService, ArtistService artistService, AlbumService albumService) {
        this.trackService = trackService;
        this.artistService = artistService;
        this.albumService = albumService;
    }

    @PostMapping("/add")
    public Track addTrack(@RequestParam String title,
                          @RequestParam String duration,
                          @RequestParam Long artistId,
                          @RequestParam Long albumId) {
        Artist artist = artistService.findById(artistId);
        Album album = albumService.findById(albumId);
        if (artist == null) throw new RuntimeException("Artist not found");
        if (album == null) throw new RuntimeException("Album not found");
        return trackService.addTrack(title, duration, artist, album);
    }

    @GetMapping
    public List<Track> getAll() {
        return trackService.getAll();
    }
}
