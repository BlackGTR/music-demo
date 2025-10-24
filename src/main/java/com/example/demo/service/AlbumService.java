package com.example.demo.service;

import com.example.demo.model.Album;
import com.example.demo.model.Artist;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AlbumService {
    private final List<Album> albums = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public Album addAlbum(String title, Artist artist) {
        Album album = new Album();
        album.setId(nextId.getAndIncrement());
        album.setTitle(title);
        album.setArtist(artist);
        albums.add(album);
        artist.getAlbums().add(album);
        return album;
    }

    public List<Album> getAll() {
        return albums;
    }

    public Album findById(Long id) {
        return albums.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }
}
