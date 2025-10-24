package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Track {
    private Long id;
    private String title;
    private String duration;

    @JsonBackReference
    private Artist artist;

    @JsonBackReference
    private Album album;
}
