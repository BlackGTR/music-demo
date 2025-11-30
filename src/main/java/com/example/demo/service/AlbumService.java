package com.example.demo.service;

import com.example.demo.model.Album;
import com.example.demo.model.Artist;
import com.example.demo.repository.AlbumRepository;
import com.example.demo.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public AlbumService(AlbumRepository albumRepository,
                        ArtistRepository artistRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    public Album addAlbum(String title, Artist artist) {
        Album album = new Album();
        album.setTitle(title);
        album.setArtist(artist);
        return albumRepository.save(album);
    }

    public List<Album> findByArtistId(Long artistId) {
        return albumRepository.findByArtistId(artistId);
    }
    public List<Album> getAll() {
        return albumRepository.findAll();
    }

    public Album findById(Long id) {
        return albumRepository.findById(id).orElse(null);
    }
    public Album update(Long id, String title, Long artistId) {
        Album album = findById(id);

        if (title != null) {
            album.setTitle(title);
        }
        if (artistId != null) {
            Artist artist = artistRepository.findById(artistId)
                    .orElseThrow(() -> new RuntimeException("Artist not found"));
            album.setArtist(artist);
        }

        return albumRepository.save(album);
    }
    public void delete(Long id) {
        albumRepository.deleteById(id);
    }
}
