package com.example.demo.service;

import com.example.demo.model.Track;
import com.example.demo.model.Artist;
import com.example.demo.model.Album;
import com.example.demo.repository.TrackRepository;
import com.example.demo.repository.ArtistRepository;
import com.example.demo.repository.AlbumRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    public TrackService(TrackRepository trackRepository,
                        ArtistRepository artistRepository,
                        AlbumRepository albumRepository) {
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
    }
    public Track addTrack(String title, String duration, Artist artist, Album album) {
        Track track = new Track();
        track.setTitle(title);
        track.setDuration(duration);
        track.setArtist(artist);
        track.setAlbum(album);
        return trackRepository.save(track);
    }

    public List<Track> getAll() {
        return trackRepository.findAll();
    }

    public Track findById(Long id) {
        return trackRepository.findById(id).orElse(null);
    }
    public Track update(Long id,
                        String title,
                        String duration,
                        Long artistId,
                        Long albumId) {

        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        if (title != null) track.setTitle(title);
        if (duration != null) track.setDuration(duration);

        if (artistId != null) {
            Artist artist = artistRepository.findById(artistId)
                    .orElseThrow(() -> new RuntimeException("Artist not found"));
            track.setArtist(artist);
        }

        if (albumId != null) {
            Album album = albumRepository.findById(albumId)
                    .orElseThrow(() -> new RuntimeException("Album not found"));
            track.setAlbum(album);
        }

        return trackRepository.save(track);
    }

    public void delete(Long id) {
        trackRepository.deleteById(id);
    }
}
