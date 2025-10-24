package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Artist {
    private Long id;
    private String name;

    @JsonManagedReference
    private List<Album> albums = new ArrayList<>();

    @JsonManagedReference
    private List<Track> tracks = new ArrayList<>();
}
