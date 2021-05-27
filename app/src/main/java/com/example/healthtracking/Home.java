package com.example.healthtracking;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

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

