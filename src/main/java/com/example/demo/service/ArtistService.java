package com.example.demo.service;

import com.example.demo.model.Artist;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ArtistService {
    private final List<Artist> artists = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public Artist addArtist(String name) {
        Artist a = new Artist();
        a.setId(nextId.getAndIncrement());
        a.setName(name);
        artists.add(a);
        return a;
    }

    public List<Artist> getAll() {
        return artists;
    }

    public Artist findById(Long id) {
        return artists.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }
}
