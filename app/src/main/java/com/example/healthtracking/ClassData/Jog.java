package com.example.healthtracking.ClassData;

import java.util.TreeMap;

public class Jog {
    public  double Distance;
    public  double Calories;
    public  int StepCount;
    public int Time;
    //public TreeMap<String, DetailJog > Detail;

    public Jog()
    {
      //  Detail = new TreeMap<String, DetailJog>();
        Distance = 0;
        Calories = 0;
        StepCount = 0;
        Time = 0;

    }

    public Jog(double distance, double calories, int stepCount, int time) {
        Distance = distance;
        Calories = calories;
        StepCount = stepCount;
        Time = time;
      //  Detail = new TreeMap<String, DetailJog>();
    }
}
