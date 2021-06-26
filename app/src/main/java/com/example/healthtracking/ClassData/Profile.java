package com.example.healthtracking.ClassData;

public class Profile {
    public String Name;
    public  int Age;
    public  String Sex;
    public int Height;
    public int Weight;
    public PeriodTracking DetailPeriod;

    public  Profile()
    {

    }

    public Profile(String name, int age, String sex, int height, int weight, PeriodTracking detailPeriod) {
        Name = name;
        Age = age;
        Sex = sex;
        Height = height;
        Weight = weight;
        DetailPeriod = detailPeriod;
    }
}
