package com.example.healthtracking.CardView;

import android.widget.ProgressBar;

public class Food {
    private double calories;
    private int lit;
    private ProgressBar progressBar;

    public Food(double calories, int lit, ProgressBar progressBar){
        this.calories = calories;
        this.lit = lit;
        this.progressBar = progressBar;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getLit() {
        return lit;
    }

    public void setLit(int lit) {
        this.lit = lit;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
