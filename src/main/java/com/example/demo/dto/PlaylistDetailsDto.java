package com.example.demo.dto;

import java.util.List;

public class PlaylistDetailsDto {
    private Long id;
    private String name;
    private String username;
    private int trackCount;
    private List<String> trackTitles;
    private List<String> artistNames;
    private List<String> albumTitles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public List<String> getTrackTitles() {
        return trackTitles;
    }

    public void setTrackTitles(List<String> trackTitles) {
        this.trackTitles = trackTitles;
    }

    public List<String> getArtistNames() {
        return artistNames;
    }

    public void setArtistNames(List<String> artistNames) {
        this.artistNames = artistNames;
    }

    public List<String> getAlbumTitles() {
        return albumTitles;
    }

    public void setAlbumTitles(List<String> albumTitles) {
        this.albumTitles = albumTitles;
    }
}
