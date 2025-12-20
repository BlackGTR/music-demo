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
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonIgnore // чтобы пароль не улетал в JSON
    private String password;

    // ДОБАВИЛИ: роль (ROLE_USER / ROLE_ADMIN)
    @Column(nullable = false)
    private String role;

    @Column(nullable = false, unique = true)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore          // КЛЮЧЕВАЯ СТРОКА: не сериализуем плейлисты внутри пользователя
    private List<Playlist> playlists = new ArrayList<>();
}
