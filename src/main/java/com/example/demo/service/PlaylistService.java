package com.example.demo.service;

import com.example.demo.model.Playlist;
import com.example.demo.model.Track;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PlaylistService {
    private final List<Playlist> playlists = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public Playlist addPlaylist(String name, User user) {
        Playlist p = new Playlist();
        p.setId(nextId.getAndIncrement());
        p.setName(name);
        p.setUser(user);
        playlists.add(p);
        user.getPlaylists().add(p);
        return p;
    }

    public void addTrack(Long playlistId, Track track) {
        playlists.stream()
                .filter(p -> p.getId().equals(playlistId))
                .findFirst()
                .ifPresent(p -> p.getTracks().add(track));
    }

    public List<Playlist> getAll() {
        return playlists;
    }
}
