package com.example.healthtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

public class GoalAccountActivity extends AppCompatActivity {
    AutoCompleteTextView atvCaloriesConsumed, atvCaloriesRelease, atvWaterConsumed;
    ImageView imgSetupInGoal;
    TextView tvSetupInGoal;
    View decorateView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_goal);

        atvCaloriesConsumed = findViewById(R.id.atvCaloriesConsumed);
        atvCaloriesRelease = findViewById(R.id.atvCaloriesRelease);
        atvWaterConsumed = findViewById(R.id.atvWaterConsumed);
        imgSetupInGoal = findViewById(R.id.imgSetupInGoal);
        tvSetupInGoal = findViewById(R.id.tvSetupInGoal);

        decorView();
        setButtonRecord();
    }
    public void decorView(){
        decorateView = getWindow().getDecorView();
        decorateView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorateView.setSystemUiVisibility(hideSystemBar());
                }
            }
        });
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorateView.setSystemUiVisibility(hideSystemBar());
        }
    }
    private int hideSystemBar() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    private void setButtonRecord(){
        imgSetupInGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
            }
        });
        tvSetupInGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
            }
        });
    }
    private void record(){

    }
}