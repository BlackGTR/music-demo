package com.example.demo.service;

import com.example.demo.model.Album;
import com.example.demo.model.Artist;
import com.example.demo.model.Track;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TrackService {
    private final List<Track> tracks = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public Track addTrack(String title, String duration, Artist artist, Album album) {
        Track track = new Track();
        track.setId(nextId.getAndIncrement());
        track.setTitle(title);
        track.setDuration(duration);
        track.setArtist(artist);
        track.setAlbum(album);
        tracks.add(track);
        artist.getTracks().add(track);
        album.getTracks().add(track);
        return track;
    }

    public List<Track> getAll() {
        return tracks;
    }

    public Track findById(Long id) {
        return tracks.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }
}
