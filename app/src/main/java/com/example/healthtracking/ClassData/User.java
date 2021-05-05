package com.example.healthtracking.ClassData;

import java.util.TreeMap;

public class User {
    public Profile profile;
    public TreeMap<String, OnedayofPractice > practice;

    public User()
    {
        practice = new TreeMap<String, OnedayofPractice>();
    }

    public User(Profile profile, TreeMap<String, OnedayofPractice> practice) {
        practice = new TreeMap<String, OnedayofPractice>();
        this.profile = profile;
        this.practice = practice;
    }
}
