package com.example.healthtracking.ClassData;

public class DetailNutrition {
    public  String NameFood;
    public  String UnitFood;
    public double Calories;

    public  DetailNutrition()
    {
        NameFood = "";
        UnitFood = "";
        Calories = 0;
    }

    public DetailNutrition(String nameFood, String unitFood, double calories) {
        NameFood = nameFood;
        UnitFood = unitFood;
        Calories = calories;
    }
}
