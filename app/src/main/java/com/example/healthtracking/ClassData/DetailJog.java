package com.example.healthtracking.ClassData;

public class DetailJog {
    public  double Distance;
    public  double Calories;
    public  int StepCount;
    public int Time;

    public DetailJog()
    {}

    public DetailJog(double distance, double calories, int stepCount, int time) {
        Distance = distance;
        Calories = calories;
        StepCount = stepCount;
        Time = time;
    }
}
