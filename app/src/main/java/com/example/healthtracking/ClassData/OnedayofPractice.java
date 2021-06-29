package com.example.healthtracking.ClassData;

public class OnedayofPractice {
    public  Run run;
    public Nutrition nutrition;
    public  int firefitday;
    public  Jog jog;
    public Exercise exercise;

    public  OnedayofPractice()
    {

    }

    public OnedayofPractice(Run run, Nutrition nutrition, int firefitday, Jog jog, Exercise exercise) {
        this.run = run;
        this.nutrition = nutrition;
        this.firefitday = firefitday;
        this.jog = jog;
        this.exercise = exercise;
    }
}

