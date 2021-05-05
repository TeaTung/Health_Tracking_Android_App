package com.example.healthtracking.ClassData;

public class Profile {
    public String Name;
    public  int Age;
    public  String Sex;
    public double Height;
    public double Weight;

    public  Profile()
    {

    }

    public Profile(String name, int age, String sex, double height, double weight) {
        Name = name;
        Age = age;
        Sex = sex;
        Height = height;
        this.Weight = weight;
    }
}
