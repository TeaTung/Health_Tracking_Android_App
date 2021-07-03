package com.example.healthtracking;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.healthtracking.CheckFireFitDay.CheckFireFitDay;
import com.example.healthtracking.ClassData.DetailExercise;
import com.example.healthtracking.ClassData.DetailJog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class DoExercise extends AppCompatActivity implements SensorEventListener {
    View decorateView;
    String exerciseName;
    TextView tvTopicDoingEx, tvTimeRecordDoingEx, tvStop, tvFireFit, tvRecordKalos, tvCountTime, tvCancel;
    ImageView imgDoingEx, imgStop, imgCancel;
    ProgressBar pgbAwardEx;
    Timer timer;
    TimerTask timerTask;
    EditText edtCalories, edtTimeCount ;
    private SensorManager sensorManager;
    private Sensor sensor;

    double calories;
    double caloriesOneTime;
    int count = 0;
    int time = 0, time1 = 0;
    boolean isSensorUsed;
    String Stime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_exercise);

        tvCountTime = findViewById(R.id.tvCountTime);
        tvFireFit = findViewById(R.id.tvFireFit);
        tvRecordKalos = findViewById(R.id.tvRecordKalos);
        tvStop = findViewById(R.id.tvStop);
        tvCancel = findViewById(R.id.tvCancel);
        tvTimeRecordDoingEx = findViewById(R.id.tvTimeRecordDoingEx);
        tvTopicDoingEx = findViewById(R.id.tvTopicDoingEx);
        imgDoingEx = findViewById(R.id.imgDoingEx);
        imgStop = findViewById(R.id.imgStop);
        imgCancel = findViewById(R.id.imgCancel);
        pgbAwardEx = findViewById(R.id.pgbAwardEx);
        exerciseName = getIntent().getStringExtra("Name");

        isSensorUsed =  getIntent().getBooleanExtra("isSensorUsed",true);
        timer = new Timer();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        pgbAwardEx.setMax(100);
        edtCalories = findViewById(R.id.edtCalories);
        edtTimeCount = findViewById(R.id.edtTimeCount);
        edtTimeCount.setVisibility(View.INVISIBLE);
        edtCalories.setVisibility(View.INVISIBLE);

        setEventEditText();
        decorView();
        createDetailExercise();
        Stime = getStime();
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
                tvRecordKalos.setText(Math.round(calories) + " kcal");
                tvCountTime.setText(Math.round(count )+ " Lần");
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
            Drawable pic = ResourcesCompat.getDrawable(getResources(), R.drawable.exercise, null);
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

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCancel.setVisibility(View.INVISIBLE);
                tvCancel.setVisibility(View.INVISIBLE);
                tvStop.setText("BẮT ĐẦU LẠI...");
                pgbAwardEx.setProgress(0);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgCancel.setVisibility(View.INVISIBLE);
                tvCancel.setVisibility(View.INVISIBLE);
                tvStop.setText("BẮT ĐẦU LẠI...");
                pgbAwardEx.setProgress(0);
            }
        });
    }
    private void stopDoingEx(){
        if (tvStop.getText().equals("DỪNG TẬP")){
            timerTask.cancel();
            time1 = time;
            time = 0;
            tvStop.setText("GHI NHẬN");
            imgCancel.setVisibility(View.VISIBLE);
            tvCancel.setVisibility(View.VISIBLE);
            edtCalories.setVisibility(View.VISIBLE);
            edtTimeCount.setVisibility(View.VISIBLE);
            tvCountTime.setVisibility(View.INVISIBLE);
            tvRecordKalos.setVisibility(View.INVISIBLE);
            edtTimeCount.setText(String.valueOf(Math.round(count)));
            edtCalories.setText(String.valueOf(Math.round(calories)));
            if (isSensorUsed == false){
                if (exerciseName.equals("Khác"))
                {
                    edtCalories.setVisibility(View.VISIBLE);
                    edtTimeCount.setVisibility(View.VISIBLE);
                }
                else {
                    edtCalories.setVisibility(View.VISIBLE);
                    edtTimeCount.setVisibility(View.VISIBLE);
                    edtCalories.setEnabled(false);
                }
            }
        } else if (tvStop.getText().equals("GHI NHẬN")){
            if (edtTimeCount.getText().toString().equals(""))
            {
                edtTimeCount.setError("Nhập số lần");
                return;
            }
            if (edtCalories.getText().toString().equals(""))
            {
                edtCalories.setError("Nhập số kcal");
                return;
            }

            tvTimeRecordDoingEx.setText(formatTime(0,0,0));
            recordExercise();
            ///
            imgCancel.setVisibility(View.INVISIBLE);
            tvCancel.setVisibility(View.INVISIBLE);
            tvStop.setText("BẮT ĐẦU LẠI...");
            pgbAwardEx.setProgress(0);
            Toast.makeText(DoExercise.this, "Ghi nhận thành công", Toast.LENGTH_SHORT).show();
        } else if (tvStop.getText().equals("BẮT ĐẦU LẠI...")){
            tvStop.setText("DỪNG TẬP");
            count = 0;
            calories = 0;
            Stime = getStime();
            startTimer();
            if (isSensorUsed == false)
            {
                tvRecordKalos.setText("0 Kalories");
                tvCountTime.setText("0 lần");
                edtCalories.setVisibility(View.INVISIBLE);
                edtTimeCount.setVisibility(View.INVISIBLE);
                edtCalories.setText("");
                edtTimeCount.setText("");
                edtTimeCount.setFocusable(false);
            }
            else {
                tvRecordKalos.setText("0 Kalories");
                tvCountTime.setText("0 lần");
                edtCalories.setVisibility(View.INVISIBLE);
                edtTimeCount.setVisibility(View.INVISIBLE);
                tvRecordKalos.setVisibility(View.VISIBLE);
                tvCountTime.setVisibility(View.VISIBLE);
                edtCalories.setText("");
                edtTimeCount.setText("");
                edtTimeCount.setFocusable(false);
            }
        }
    }
    private void setCaloriesOneTime(){
        if (exerciseName.equals("Hít đất")){
            caloriesOneTime = 4.0;
        } else if (exerciseName.equals("Gập bụng")){
            caloriesOneTime = 3.0;
        } else if (exerciseName.equals("Hít xà")){
            caloriesOneTime = 4.0;
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

    private  void setEventEditText()
    {
        edtTimeCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int countt) {
                    try {
                        if (exerciseName.equals("Khác"))
                        {
                            count = Integer.parseInt(edtTimeCount.getText().toString());
                        }
                        else {
                            count = Integer.parseInt(edtTimeCount.getText().toString());
                            calories = count * caloriesOneTime;
                            edtCalories.setText(Math.round(calories) + " kcal");
                        }
                    } catch (NumberFormatException e)
                    {

                    }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void recordExercise(){
        long millis = System.currentTimeMillis() ;
        long millis2 = System.currentTimeMillis() - 24*60*60*1000 ;
        java.sql.Date date = new java.sql.Date(millis);
        java.sql.Date date2 = new java.sql.Date(millis2);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            DetailExercise detailExercise = new DetailExercise(exerciseName,time1 ,count, calories);
            FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString()).child("exercise")
                    .child("detail").child(Stime).setValue(detailExercise);
            ///////
            FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString()).child("exercise")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            int exTime = snapshot.child("Time").getValue(Integer.class)+time1;
                            double exCalo = snapshot.child("Calories").getValue(double.class)+ calories;

                            FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                                    .child("exercise").child("Calories").setValue(exCalo);
                            FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                                    .child("exercise").child("Time").setValue(exTime);

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        CheckFireFitDay checkFireFitDay = new CheckFireFitDay();
        checkFireFitDay.CheckOneDay(date.toString(), date2.toString());


    }

    public String getStime()
    {
        Calendar calendar = Calendar.getInstance();
        String hour = (calendar.getTime().getHours() > 9) ?
                "" + calendar.getTime().getHours() + ""
                : "0" + calendar.getTime().getHours();
        String minute = (calendar.getTime().getMinutes() > 9) ?
                "" + calendar.getTime().getMinutes() + ""
                : "0" + calendar.getTime().getMinutes();
        String second = (calendar.getTime().getSeconds() > 9) ?
                "" + calendar.getTime().getSeconds() + ""
                : "0" + calendar.getTime().getSeconds();
        return hour + ":" + minute + ":" + second;
    }


}