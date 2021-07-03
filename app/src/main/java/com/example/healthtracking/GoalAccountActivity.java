package com.example.healthtracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthtracking.CheckFireFitDay.CheckFireFitDay;
import com.example.healthtracking.ClassData.DetailJog;
import com.example.healthtracking.ClassData.Goal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

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
        Loaddata();
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

    public void Loaddata()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("profile").child("DayGoal")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        atvCaloriesConsumed.setText(""+snapshot.child("IntakeCalories").getValue(Integer.class));
                        atvCaloriesRelease.setText(""+snapshot.child("ExerciseCalories").getValue(Integer.class));
                        atvWaterConsumed.setText(""+snapshot.child("Water").getValue(Integer.class));

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }
    private void record(){
        if (atvCaloriesConsumed.getText().toString().equals(""))
        {
            atvCaloriesConsumed.setError("Nhập lượng calo nạp vào");
            return;
        }
        if (atvCaloriesRelease.getText().toString().equals(""))
        {
            atvCaloriesRelease.setError("Nhập lượng calo tieu thu");
            return;
        }
        if (atvWaterConsumed.getText().toString().equals(""))
        {
            atvWaterConsumed.setError("Nhập lượng nước nạp vào");
            return;
        }

        Goal goal = new Goal(Integer.parseInt(atvCaloriesConsumed.getText().toString()),
                Integer.parseInt(atvCaloriesRelease.getText().toString()), Integer.parseInt(atvWaterConsumed.getText().toString()));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("profile").child("DayGoal").setValue(goal);
        Toast.makeText(this, "Thiết lập thành công", Toast.LENGTH_SHORT).show();
        long millis = System.currentTimeMillis() ;
        long millis2 = System.currentTimeMillis() - 24*60*60*1000 ;
        java.sql.Date date = new java.sql.Date(millis);
        java.sql.Date date2 = new java.sql.Date(millis2);
        CheckFireFitDay checkFireFitDay = new CheckFireFitDay();
        checkFireFitDay.CheckOneDay(date.toString(), date2.toString());
    }
}