package com.example.calmable.sample;

public class Post {

    private int sHeartRate;

    public Post() {
    }

    public Post(int sHeartRate) {
        this.sHeartRate = sHeartRate;
    }

    public int getsHeartRate() {
        return sHeartRate;
    }

    public void setsHeartRate(int sHeartRate) {
        this.sHeartRate = sHeartRate;
    }


    @Override
    public String toString() {
        return "Post{" +
                "sHeartRate=" + sHeartRate +
                '}';
    }
}
