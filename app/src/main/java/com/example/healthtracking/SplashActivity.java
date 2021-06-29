package com.example.healthtracking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthtracking.ClassData.UserSetting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    //Variables
    SharedPreferences sharedPreferences;
    Animation logoAnimation, textAnimation;
    ImageView logoImage;
    TextView logoText;
    int realStepCounter;
    View decorateView;

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
        SetStepcout();
        UserSetting userSetting = new UserSetting();
        getUserSetting(userSetting);
        decorView();

        new Handler().postDelayed((Runnable) () -> {
            if (userSetting.wasInformation) {
                Intent intent = new Intent(SplashActivity.this, InformationActivity.class);
                startActivity(intent);
                finish();
            } else if (userSetting.wasLogin){
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if (userSetting.wasLogout)
            {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(SplashActivity.this, OnBoarding.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
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
    public void getUserSetting(UserSetting userSetting){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        userSetting.wasLogin = sharedPreferences.getBoolean("WAS_LOGIN",false);
        userSetting.wasInformation = sharedPreferences.getBoolean("WAS_INFORMATION",false);
        userSetting.wasLogout = sharedPreferences.getBoolean("WAS_LOGOUT",false);
        //userSetting.UID = sharedPreferences.getString("UID","");
    }

    public void SetStepcout()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        if (user != null)
        {
            long millis = System.currentTimeMillis();
            java.sql.Date date = new java.sql.Date(millis);
            FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            if (snapshot.getValue() != null)
                            {
                                realStepCounter = snapshot.child("run").child("StepCount").getValue(Integer.class);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("REALSTEP", realStepCounter);
                                editor.apply();
                            }
                            else
                            {
                                realStepCounter = 0;
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("REALSTEP", realStepCounter);
                                editor.apply();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }
    }


}