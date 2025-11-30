package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // Много альбомов — один артист
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    @JsonIgnore          // ← ВАЖНО: не сериализуем артиста внутри альбома, чтобы не было рекурсии и проблем с прокси
    private Artist artist;

    // Один альбом — много треков
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Track> tracks = new ArrayList<>();
}
