package com.example.healthtracking.ClassData;

public class DetailExercise {
    public  String NameExercise;
    public int Time;
    public  int Rep;
    public  double Calories;

    public DetailExercise() {
        this.NameExercise = "";
        this.Time = 0;
        this.Rep =0;
        this.Calories = 0;

    }

    public DetailExercise(String nameExervise, int time, int rep, double calories) {
        this.NameExercise = nameExervise;
        this.Time = time;
        this.Rep = rep;
        this.Calories = calories;
    }
}
