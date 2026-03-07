package com.example.demo.service;

import com.example.demo.model.Album;
import com.example.demo.model.Artist;
import com.example.demo.model.Track;
import com.example.demo.repository.TrackRepository;
import com.example.demo.service.PlaylistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final PlaylistService playlistService;

    public TrackService(TrackRepository trackRepository,
                        PlaylistService playlistService) {
        this.trackRepository = trackRepository;
        this.playlistService = playlistService;
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

    @Transactional
    public void delete(Long id) {
        playlistService.removeTrackFromAllPlaylists(id);
        trackRepository.deleteById(id);
    }
}
