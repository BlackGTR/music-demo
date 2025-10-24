package com.example.demo.controller;

import com.example.demo.model.Album;
import com.example.demo.model.Artist;
import com.example.demo.service.AlbumService;
import com.example.demo.service.ArtistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final ArtistService artistService;

    public AlbumController(AlbumService albumService, ArtistService artistService) {
        this.albumService = albumService;
        this.artistService = artistService;
    }

    @PostMapping("/add")
    public Album addAlbum(@RequestParam String title, @RequestParam Long artistId) {
        Artist artist = artistService.findById(artistId);
        if (artist == null) throw new RuntimeException("Artist not found");
        return albumService.addAlbum(title, artist);
    }

    @GetMapping
    public List<Album> getAll() {
        return albumService.getAll();
    }
}
