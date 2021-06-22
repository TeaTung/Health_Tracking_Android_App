package com.example.healthtracking;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.Timer;
import java.util.TimerTask;

public class DoExercise extends AppCompatActivity implements SensorEventListener {
    View decorateView;
    String exerciseName;
    TextView tvTopicDoingEx, tvTimeRecordDoingEx, tvStop, tvFireFit, tvRecordKalos, tvCountTime;
    ImageView imgDoingEx, imgStop;
    ProgressBar pgbAwardEx;
    Timer timer;
    TimerTask timerTask;
    EditText edtCalories, edtTimeCount ;
    private SensorManager sensorManager;
    private Sensor sensor;

    double calories;
    double caloriesOneTime;
    double count;
    double time = 0.0;
    boolean isSensorUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exercise);

        tvCountTime = findViewById(R.id.tvCountTime);
        tvFireFit = findViewById(R.id.tvFireFit);
        tvRecordKalos = findViewById(R.id.tvRecordKalos);
        tvStop = findViewById(R.id.tvStop);
        tvTimeRecordDoingEx = findViewById(R.id.tvTimeRecordDoingEx);
        tvTopicDoingEx = findViewById(R.id.tvTopicDoingEx);
        imgDoingEx = findViewById(R.id.imgDoingEx);
        imgStop = findViewById(R.id.imgStop);
        pgbAwardEx = findViewById(R.id.pgbAwardEx);
        exerciseName = getIntent().getStringExtra("Name");
        isSensorUsed = getIntent().getBooleanExtra("isSensorUsed",true);
        timer = new Timer();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        pgbAwardEx.setMax(100);
        edtCalories = findViewById(R.id.edtCalories);
        edtTimeCount = findViewById(R.id.edtTimeCount);

        decorView();
        createDetailExercise();
        startTimer();
        createButtonStop();
        setCaloriesOneTime();
        checkUsingSensor();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isSensorUsed == true){
            float distance = event.values[0];
            if (distance < 5){
                count ++;
                calories += caloriesOneTime;
                tvRecordKalos.setText(calories + "Kalories");
                tvCountTime.setText(count + "Lần");
                pgbAwardEx.setProgress((int)calories);
                if (count == 100){
                    tvFireFit.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        sensorManager.unregisterListener(this);
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
    private void createDetailExercise(){
        if (exerciseName.equals("Hít đất")){
            tvTopicDoingEx.setText("ĐANG HÍT ĐẤT...");
            Drawable pic = ResourcesCompat.getDrawable(getResources(), R.drawable.pushup, null);
            imgDoingEx.setImageDrawable(pic);
        } else if (exerciseName.equals("Gập bụng")){
            tvTopicDoingEx.setText("ĐANG GẬP BỤNG...");
            Drawable pic = ResourcesCompat.getDrawable(getResources(), R.drawable.crunch, null);
            imgDoingEx.setImageDrawable(pic);
        } else if (exerciseName.equals("Hít xà")){
            tvTopicDoingEx.setText("ĐANG HÍT XÀ...");
            Drawable pic = ResourcesCompat.getDrawable(getResources(), R.drawable.pull_up, null);
            imgDoingEx.setImageDrawable(pic);
        }  else if (exerciseName.equals("Khác")) {
            tvTopicDoingEx.setText("ĐANG TẬP...");
            Drawable pic = ResourcesCompat.getDrawable(getResources(), R.drawable.pushup, null);
            imgDoingEx.setImageDrawable(pic);
        }
    }
    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        tvTimeRecordDoingEx.setText(getTimerText());
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,1000);
    }
    private String getTimerText() {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) /60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }
    private String formatTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }
    private void createButtonStop(){
        imgStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDoingEx();
            }
        });

        tvStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDoingEx();
            }
        });
    }
    private void stopDoingEx(){
        if (tvStop.getText().equals("DỪNG TẬP")){
            timerTask.cancel();
            time = 0.0;
            tvStop.setText("GHI NHẬN");
            if (isSensorUsed == false){
                edtCalories.setVisibility(View.VISIBLE);
                edtTimeCount.setVisibility(View.VISIBLE);
            }
        } else if (tvStop.getText().equals("GHI NHẬN")){
            tvTimeRecordDoingEx.setText(formatTime(0,0,0));
            recordExercise();
            tvStop.setText("BẮT ĐẦU LẠI...");
            pgbAwardEx.setProgress(0);
            Toast.makeText(DoExercise.this, "Ghi nhận thành công", Toast.LENGTH_SHORT).show();
        } else if (tvStop.getText().equals("BẮT ĐẦU LẠI...")){
            tvStop.setText("DỪNG TẬP");
            count = 0;
            calories = 0;
            startTimer();
            tvRecordKalos.setText("0 Kalories");
            tvCountTime.setText("0 lần");
        }
    }
    private void setCaloriesOneTime(){
        if (exerciseName.equals("Hít đất")){
            caloriesOneTime = 4;
        } else if (exerciseName.equals("Gập bụng")){
            caloriesOneTime = 3;
        } else if (exerciseName.equals("Hít xà")){
            caloriesOneTime = 4;
        }  else if (exerciseName.equals("Khác")) {

        }
    }
    private void checkUsingSensor(){
        if (isSensorUsed == false){
            tvCountTime.setVisibility(View.INVISIBLE);
            tvRecordKalos.setVisibility(View.INVISIBLE);
            edtCalories.setVisibility(View.INVISIBLE);
            edtTimeCount.setVisibility(View.INVISIBLE);
            pgbAwardEx.setVisibility(View.INVISIBLE);
        }
    }
    private void recordExercise(){
        if (isSensorUsed == false){

        }
    }
}