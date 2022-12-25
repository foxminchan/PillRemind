package com.example.pillremind.model.domain;

public class TimeDose {
    private String time;
    private int dose;

    public TimeDose(String time, int dose) {
        this.time = time;
        this.dose = dose;
    }

    public TimeDose() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }
}
