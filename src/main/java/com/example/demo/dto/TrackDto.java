package com.example.demo.dto;

import lombok.Data;

@Data
public class TrackDto {
    private Long id;
    private String title;
    private String duration;
    private Long artistId;
    private Long albumId;
}
