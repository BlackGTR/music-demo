package com.example.demo.dto;

import java.util.List;

public class CreatePlaylistRequest {
    private Long userId;
    private String name;
    private List<Long> trackIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getTrackIds() {
        return trackIds;
    }

    public void setTrackIds(List<Long> trackIds) {
        this.trackIds = trackIds;
    }
}
