package com.example.calmable.model;

public class StressRelaxModel {

    Integer relaxation, stressed;
    String timestamp;

    public StressRelaxModel() {
    }

    public StressRelaxModel(Integer relaxation, Integer stressed, String timestamp) {
        this.relaxation = relaxation;
        this.stressed = stressed;
        this.timestamp = timestamp;
    }

    public Integer getRelaxation() {
        return relaxation;
    }

    public void setRelaxation(Integer relaxation) {
        this.relaxation = relaxation;
    }

    public Integer getStressed() {
        return stressed;
    }

    public void setStressed(Integer stressed) {
        this.stressed = stressed;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
