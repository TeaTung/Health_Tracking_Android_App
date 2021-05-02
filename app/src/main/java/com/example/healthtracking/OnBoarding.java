package com.example.healthtracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.cuberto.liquid_swipe.LiquidPager;

public class OnBoarding extends AppCompatActivity {

    LiquidPager pager;
    OnBoardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        pager = findViewById(R.id.pager);
        adapter = new OnBoardingAdapter(getSupportFragmentManager(), 1);
        pager.setAdapter(adapter);
    }
}