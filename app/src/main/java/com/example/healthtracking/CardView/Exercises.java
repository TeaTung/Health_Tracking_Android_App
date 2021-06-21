package com.example.healthtracking.CardView;

import android.widget.ProgressBar;

public class Exercises {
    private int stepsCounter;
    private ProgressBar progressBar;
    private double calories;
    private double distances;

    public Exercises(int stepsCounter, ProgressBar progressBar, double calories, double distances) {
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

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getDistances() {
        return distances;
    }

    public void setDistances(double distances) {
        this.distances = distances;
    }
}
