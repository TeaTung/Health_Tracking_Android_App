package com.example.healthtracking.ClassData;

public class Run {
    public  double Distance;
    public  double Calories;
    public  int StepCount;

    public  Run()
    {

    }

    public Run(int stepCount, double quangDuong, double calories) {
        StepCount = stepCount;
        Distance = quangDuong;
        Calories = calories;

    }
}
