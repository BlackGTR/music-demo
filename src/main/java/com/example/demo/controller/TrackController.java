package com.example.demo.controller;

import com.example.demo.model.Album;
import com.example.demo.model.Artist;
import com.example.demo.model.Track;
import com.example.demo.dto.TrackDto;
import com.example.demo.service.AlbumService;
import com.example.demo.service.ArtistService;
import com.example.demo.service.TrackService;
import com.example.demo.service.PlaylistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracks")
public class TrackController {

    private final TrackService trackService;
    private final ArtistService artistService;
    private final AlbumService albumService;
    private final PlaylistService playlistService; // <— добавили поле

    public TrackController(TrackService trackService,
                           ArtistService artistService,
                           AlbumService albumService,
                           PlaylistService playlistService) { // <— добавили в конструктор
        this.trackService = trackService;
        this.artistService = artistService;
        this.albumService = albumService;
        this.playlistService = playlistService;
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

    @GetMapping("/{id}")
    public Track getById(@PathVariable Long id) {
        return trackService.findById(id);
    }
    @GetMapping("/with-ids")
    public List<TrackDto> getAllWithIds() {
        return trackService.getAll().stream().map(track -> {
            TrackDto dto = new TrackDto();
            dto.setId(track.getId());
            dto.setTitle(track.getTitle());
            dto.setDuration(track.getDuration());
            dto.setArtistId(track.getArtist() != null ? track.getArtist().getId() : null);
            dto.setAlbumId(track.getAlbum() != null ? track.getAlbum().getId() : null);
            return dto;
        }).toList();
    }
    @PutMapping("/{id}")
    public Track updateTrack(@PathVariable Long id,
                             @RequestParam(required = false) String title,
                             @RequestParam(required = false) String duration,
                             @RequestParam(required = false) Long artistId,
                             @RequestParam(required = false) Long albumId) {
        return trackService.update(id, title, duration, artistId, albumId);
    }
    // бизнес-операция №5: удалить трек из всех плейлистов
    @DeleteMapping("/{trackId}/remove-from-all-playlists")
    public void removeFromAllPlaylists(@PathVariable Long trackId) {
        playlistService.removeTrackFromAllPlaylists(trackId); // <— теперь через бин
    }
}
