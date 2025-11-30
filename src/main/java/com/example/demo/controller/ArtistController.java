package com.example.demo.controller;

import com.example.demo.model.Album;
import com.example.demo.model.Artist;
import com.example.demo.service.AlbumService;
import com.example.demo.service.ArtistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService service;
    private final AlbumService albumService; // ← добавили

    public ArtistController(ArtistService service, AlbumService albumService) { // ← добавили
        this.service = service;
        this.albumService = albumService; // ← добавили
    }

    @PostMapping("/add")
    public Artist addArtist(@RequestParam String name) {
        return service.addArtist(name);
    }

    @GetMapping
    public List<Artist> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Artist getById(@PathVariable Long id) {
        return service.findById(id);
    }

    // новый эндпоинт для получения альбомов артиста
    @GetMapping("/{artistId}/albums")
    public List<Album> getAlbumsByArtist(@PathVariable Long artistId) {
        return albumService.findByArtistId(artistId);
    }
    @PutMapping("/{id}")
    public Artist update(@PathVariable Long id,
                         @RequestParam String name) {
        return service.update(id, name);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
