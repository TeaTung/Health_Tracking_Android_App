package com.example.healthtracking.ClassData;

public class Goal {
    public double IntakeCalories;
    public double ExerciseCalories;
    public int Water;

    public Goal() {
        IntakeCalories = 0;
        ExerciseCalories = 0;
        Water = 0;
    }

    public Goal(double intakeCalories, double exerciseCalories, int water) {
        IntakeCalories = intakeCalories;
        ExerciseCalories = exerciseCalories;
        Water = water;
    }
}
