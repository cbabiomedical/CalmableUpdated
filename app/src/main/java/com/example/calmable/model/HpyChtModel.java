package com.example.calmable.model;

public class HpyChtModel {

    String id, address, time, eventNote;
    int rate;


    public HpyChtModel() {
    }

    public HpyChtModel(String id, String address, String time, String eventNote, int rate) {
        this.id = id;
        this.address = address;
        this.time = time;
        this.eventNote = eventNote;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEventNote() {
        return eventNote;
    }

    public void setEventNote(String eventNote) {
        this.eventNote = eventNote;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
