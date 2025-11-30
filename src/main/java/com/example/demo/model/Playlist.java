package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "playlists")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Много плейлистов — один пользователь
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    // Many-to-many playlist <-> track
    @ManyToMany
    @JoinTable(
            name = "playlist_tracks",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "track_id")
    )
    private List<Track> tracks = new ArrayList<>();
}
