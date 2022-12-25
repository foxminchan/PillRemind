package com.example.pillremind.model.domain;

public class Task {
    private String time;
    private String pillHeader;
    private String pillDescription;
    private String pillWarning;

    public Task(String time, String pillHeader, String pillDescription, String pillWarning) {
        this.time = time;
        this.pillHeader = pillHeader;
        this.pillDescription = pillDescription;
        this.pillWarning = pillWarning;
    }

    public Task() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPillHeader() {
        return pillHeader;
    }

    public void setPillHeader(String pillHeader) {
        this.pillHeader = pillHeader;
    }

    public String getPillDescription() {
        return pillDescription;
    }

    public void setPillDescription(String pillDescription) {
        this.pillDescription = pillDescription;
    }

    public String getPillWarning() {
        return pillWarning;
    }

    public void setPillWarning(String pillWarning) {
        this.pillWarning = pillWarning;
    }
}
