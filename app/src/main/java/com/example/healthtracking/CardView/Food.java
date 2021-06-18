package com.example.healthtracking.CardView;

import android.widget.ProgressBar;

public class Food {
    private int calories;
    private int lit;
    private ProgressBar progressBar;

    public Food(int calories, int lit, ProgressBar progressBar){
        this.calories = calories;
        this.lit = lit;
        this.progressBar = progressBar;
    }

    public int getCalories() {
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
