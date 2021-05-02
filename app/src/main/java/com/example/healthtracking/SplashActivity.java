package com.example.healthtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

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

        new Handler().postDelayed((Runnable) () -> {
            Intent intent = new Intent(SplashActivity.this, OnBoarding.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN);
    }
}