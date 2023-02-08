package com.example.calmable.model;

public class CalmChart {

    String songName;
    String songUrl;
    String category;
    int timeToRelaxIndex;

    public CalmChart() {

    }

    public CalmChart(String songName, String songUrl, String category, int timeToRelaxIndex) {
        this.songName = songName;
        this.songUrl = songUrl;
        this.category = category;
        this.timeToRelaxIndex = timeToRelaxIndex;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public int getTimeToRelaxIndex() {
        return timeToRelaxIndex;
    }

    public void setTimeToRelaxIndex(int timeToRelaxIndex) {
        this.timeToRelaxIndex = timeToRelaxIndex;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
