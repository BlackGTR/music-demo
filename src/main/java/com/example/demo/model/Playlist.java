package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Playlist {
    private Long id;
    private String name;

    @JsonBackReference
    private User user;

    @JsonManagedReference
    private List<Track> tracks = new ArrayList<>();
}
