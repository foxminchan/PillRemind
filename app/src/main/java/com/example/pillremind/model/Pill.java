package com.example.pillremind.model;

public class Pill {
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String drinkTime;
    private String imageUri;
    private int amount;
    private String unit;
    private String frequency;

    public Pill(String description, String startDate, String endDate, String drinkTime, String imageUri, int amount, String unit, String frequency) {
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.drinkTime = drinkTime;
        this.imageUri = imageUri;
        this.amount = amount;
        this.unit = unit;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDrinkTime() {
        return drinkTime;
    }

    public void setDrinkTime(String drinkTime) {
        this.drinkTime = drinkTime;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
