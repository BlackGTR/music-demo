package com.example.demo.service;

import com.example.demo.model.Album;
import com.example.demo.model.Artist;
import com.example.demo.model.Track;
import com.example.demo.repository.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackService {

    private final TrackRepository trackRepository;

    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public Track addTrack(String title, String duration, Artist artist, Album album) {
        Track track = new Track();
        track.setTitle(title);
        track.setDuration(duration);
        track.setArtist(artist);
        track.setAlbum(album);
        return trackRepository.save(track);
    }
    public Track update(Long id, String title, String duration) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Track not found"));

        track.setTitle(title);
        track.setDuration(duration);
        return trackRepository.save(track);
    }

    public List<Track> getAll() {
        return trackRepository.findAll();
    }

    public Track findById(Long id) {
        return trackRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        trackRepository.deleteById(id);
    }
}
