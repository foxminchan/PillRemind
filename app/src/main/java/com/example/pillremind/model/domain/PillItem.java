package com.example.pillremind.model.domain;

import java.util.Objects;

public class PillItem {
    private String pillImage;
    private String pillName;
    private String pillFrequency;

    public PillItem(String pillImage, String pillName, String pillFrequency) {
        this.pillImage = Objects.equals(pillImage, "") ? "null" : pillImage;
        this.pillName = pillName;
        this.pillFrequency = pillFrequency;
    }

    public String getPillImage() {
        return pillImage;
    }

    public void setPillImage(String pillImage) {
        this.pillImage = pillImage;
    }

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public String getPillFrequency() {
        return pillFrequency;
    }

    public void setPillFrequency(String pillFrequency) {
        this.pillFrequency = pillFrequency;
    }
}
