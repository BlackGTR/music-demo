package com.example.demo.service;

import com.example.demo.model.Artist;
import com.example.demo.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Artist addArtist(String name) {
        Artist artist = new Artist();
        artist.setName(name);
        return artistRepository.save(artist);
    }
    public Artist updateName(Long id, String name) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found"));

        artist.setName(name);
        return artistRepository.save(artist);
    }
    public List<Artist> getAll() {
        return artistRepository.findAll();
    }

    public Artist findById(Long id) {
        return artistRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        artistRepository.deleteById(id);
    }
}
