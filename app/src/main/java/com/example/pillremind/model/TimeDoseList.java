package com.example.pillremind.model;

import com.example.pillremind.model.domain.TimeDose;

import java.util.List;

public class TimeDoseList {
    private List<TimeDose> timeDoses;

    public TimeDoseList() {
    }

    public TimeDoseList(List<TimeDose> timeDoses) {
        this.timeDoses = timeDoses;
    }

    public List<TimeDose> getTimeDoses() {
        return timeDoses;
    }

    public void setTimeDoses(List<TimeDose> timeDoses) {
        this.timeDoses = timeDoses;
    }

    public void addTimeDose(TimeDose timeDose) {
        timeDoses.add(timeDose);
    }

    public void removeTimeDose(TimeDose timeDose) {
        timeDoses.remove(timeDose);
    }

    public void removeTimeDose(int index) {
        timeDoses.remove(index);
    }

    public void clear() {
        timeDoses.clear();
    }

    public boolean isEmpty() {
        return timeDoses.isEmpty();
    }

    public int size() {
        return timeDoses.size();
    }

    public void add(TimeDose timeDose) {
        timeDoses.add(timeDose);
    }

    public String getTimes(int index) {
        return timeDoses.get(index).getTime();
    }

    public int getDoses(int index) {
        return timeDoses.get(index).getDose();
    }
}
