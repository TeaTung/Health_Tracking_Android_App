package com.example.healthtracking.ClassData;

public class OnedayofPractice {
    public  Run run;
    public Nutrition nutrition;
    public  double sleep;
    public  Jog jog;

    public  OnedayofPractice()
    {

    }

    public OnedayofPractice(Run run, Nutrition nutrition, double sleep, Jog jog) {
        this.run = run;
        this.nutrition = nutrition;
        this.sleep = sleep;
        this.jog = jog;
    }
}
