package com.example.demo.repository;

import com.example.demo.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    // ВАЖНО — добавляем метод
    List<Album> findByArtistId(Long artistId);
}
