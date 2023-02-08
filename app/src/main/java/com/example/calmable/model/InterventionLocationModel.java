package com.example.calmable.model;

public class InterventionLocationModel {

    String Date, address, songName;

    public InterventionLocationModel() {
    }

    public InterventionLocationModel(String date, String address, String songName) {
        Date = date;
        this.address = address;
        this.songName = songName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
