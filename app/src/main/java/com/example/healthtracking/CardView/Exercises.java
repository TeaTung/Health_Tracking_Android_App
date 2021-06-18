package com.example.healthtracking.CardView;

import android.widget.ProgressBar;

public class Exercises {
    private int stepsCounter;
    private ProgressBar progressBar;
    private int calories;
    private int distances;

    public Exercises(int stepsCounter, ProgressBar progressBar, int calories, int distances) {
        this.stepsCounter = stepsCounter;
        this.progressBar = progressBar;
        this.calories = calories;
        this.distances = distances;
    }

    public int getStepsCounter() {
        return stepsCounter;
    }

    public void setStepsCounter(int stepsCounter) {
        this.stepsCounter = stepsCounter;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getDistances() {
        return distances;
    }

    public void setDistances(int distances) {
        this.distances = distances;
    }
}
