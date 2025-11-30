package com.example.demo.repository;

import com.example.demo.model.Track;
import com.example.demo.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findByAlbum(Album album);
}
