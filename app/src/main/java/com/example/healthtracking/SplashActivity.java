package com.example.healthtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthtracking.ClassData.UserSetting;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    //Variables
    Animation logoAnimation, textAnimation;
    ImageView logoImage;
    TextView logoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //Hooks
        logoImage = findViewById(R.id.logoIv);
        logoText = findViewById(R.id.logoTv);
        //Animations
        logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        textAnimation = AnimationUtils.loadAnimation(this, R.anim.text_animation);
        logoImage.setAnimation(logoAnimation);
        logoText.setAnimation(textAnimation);

        UserSetting userSetting = new UserSetting();
        getUserSetting(userSetting);


        new Handler().postDelayed((Runnable) () -> {
            if (userSetting.wasInformation) {
                Intent intent = new Intent(SplashActivity.this, InformationActivity.class);
                startActivity(intent);
                finish();
            } else if (userSetting.wasLogin){
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, OnBoarding.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }

    public void getUserSetting(UserSetting userSetting){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        userSetting.wasLogin = sharedPreferences.getBoolean("WAS_LOGIN",false);
        userSetting.wasInformation = sharedPreferences.getBoolean("WAS_INFORMATION",false);
        //userSetting.UID = sharedPreferences.getString("UID","");
    }
}