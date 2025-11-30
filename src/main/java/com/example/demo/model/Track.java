package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "tracks")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // можно хранить как строку, для простоты
    @Column(nullable = false)
    private String duration;

    // Много треков -> один артист
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    @JsonIgnore                 // чтобы в JSON не тащить артиста (и не ловить прокси/рекурсию)
    private Artist artist;

    // Много треков -> один альбом
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    @JsonIgnore                 // то же самое для альбома
    private Album album;

    // Многие-ко-многим с плейлистами
    @ManyToMany(mappedBy = "tracks")
    @JsonIgnore                 // не сериализуем плейлисты внутри трека
    private List<Playlist> playlists = new ArrayList<>();
}
