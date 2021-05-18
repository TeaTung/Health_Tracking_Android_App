package com.example.healthtracking;


public class Home {
    private int stepsCounter;
    private int kilometers;
    private int calories;

    public static final Home[] homes = {
            new Home(1,2,3)
    };

    private Home(int stepsCounter, int distance, int calories){
        this.stepsCounter = stepsCounter;
        this.kilometers = distance;
        this.calories = calories;
    }

    public int getStepsCounter() {
        return stepsCounter;
    }

    public int getKilometers() {
        return kilometers;
    }

    public int getCalories() {
        return calories;
    }
}

