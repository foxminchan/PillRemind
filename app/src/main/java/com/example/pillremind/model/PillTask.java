package com.example.pillremind.model;

public class PillTask {
    private int timesADayTaken;
    private int timesADay;

    public PillTask(int timesADayTaken, int timesADay) {
        this.timesADayTaken = timesADayTaken;
        this.timesADay = timesADay;
    }

    public PillTask() {

    }

    public int getTimesADayTaken() {
        return timesADayTaken;
    }

    public void setTimesADayTaken(int timesADayTaken) {
        this.timesADayTaken = timesADayTaken;
    }

    public int getTimesADay() {
        return timesADay;
    }

    public void setTimesADay(int timesADay) {
        this.timesADay = timesADay;
    }
}
