package com.example.healthtracking;


import android.widget.ProgressBar;

public class Home {
    private int stepsCounter;
    private ProgressBar progressBar;


    public Home(int stepsCounter, ProgressBar progressBar){
        this.stepsCounter = stepsCounter;
        this.progressBar = progressBar;
    }

    public int getStepsCounter() {
        return stepsCounter;
    }
    public ProgressBar progressBar(){
        return  progressBar;
    }
}

