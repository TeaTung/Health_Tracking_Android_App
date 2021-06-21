package com.example.healthtracking.ClassData;

public class Profile {
    public String Name;
    public  int Age;
    public  String Sex;
    public int Height;
    public int Weight;
    public  int DayKn;

    public  Profile()
    {

    }

    public Profile(String name, int age, String sex, int height, int weight, int daykn) {
        Name = name;
        Age = age;
        Sex = sex;
        Height = height;
        this.Weight = weight;
        DayKn = daykn;
    }
}
