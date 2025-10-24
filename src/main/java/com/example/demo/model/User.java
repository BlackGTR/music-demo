package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class User {
    private Long id;
    private String username;

    private List<Playlist> playlists = new ArrayList<>();
}
