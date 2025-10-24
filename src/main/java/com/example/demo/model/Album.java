package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Album {
    private Long id;
    private String title;

    @JsonBackReference
    private Artist artist;

    @JsonManagedReference
    private List<Track> tracks = new ArrayList<>();
}
