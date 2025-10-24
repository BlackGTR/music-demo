package com.example.demo.controller;

import com.example.demo.model.Artist;
import com.example.demo.service.ArtistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistService service;

    public ArtistController(ArtistService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public Artist addArtist(@RequestParam String name) {
        return service.addArtist(name);
    }

    @GetMapping
    public List<Artist> getAll() {
        return service.getAll();
    }
}
