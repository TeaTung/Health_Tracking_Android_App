package com.example.healthtracking.CardView;

public class PersonalInformation {
    private int height;
    private int weight;
    private int day;

    public PersonalInformation(int height, int weight, int day){
        this.day = day;
        this.height = height;
        this.weight = weight;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
