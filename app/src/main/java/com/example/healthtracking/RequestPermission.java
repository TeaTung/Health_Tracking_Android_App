package com.example.healthtracking;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class RequestPermission extends AppCompatActivity {
    View decorateView;
    String exerciseName;
    TextView tvTopicEx, tvHowtoUse, tvStartEx, tvNotUse;
    ImageView imgEx, imgStartEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permission);

        tvTopicEx = (TextView) findViewById(R.id.tvTopicEx);
        tvHowtoUse = (TextView) findViewById(R.id.tvHowtoUse);
        tvStartEx = (TextView) findViewById(R.id.tvStartEx);
        tvNotUse = (TextView) findViewById(R.id.tvNotUse);
        imgEx = (ImageView) findViewById(R.id.imgEx);
        imgStartEx = (ImageView) findViewById(R.id.imgStartEx);

        exerciseName = getIntent().getStringExtra("Name");
        decorView();
        createDetailExercise();
        setButtonStart();
        setButtonNotUseSensor();
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
            tvTopicEx.setText("HÍT ĐẤT CÙNG FIREFIT");
            Drawable pic = ResourcesCompat.getDrawable(getResources(), R.drawable.pushup, null);
            imgEx.setImageDrawable(pic);
            tvHowtoUse.setText("Đặt điện thoại ngang ngực FireFit sẽ dùng cảm biến tiệm cận để có thể ghi nhận quá trình luyện tập cũng như đảm bảo tư thế hít đất");
        } else if (exerciseName.equals("Gập bụng")){
            tvTopicEx.setText("GẬP BỤNG CÙNG FIREFIT");
            Drawable pic = ResourcesCompat.getDrawable(getResources(), R.drawable.crunch, null);
            imgEx.setImageDrawable(pic);
            tvHowtoUse.setText("Cầm điện thoại và chạm vào màn hình mỗi lần gập bụng để ghi nhận phiên tập");
        } else if (exerciseName.equals("Hít xà")){
            tvTopicEx.setText("HÍT XÀ CÙNG FIREFIT");
            Drawable pic = ResourcesCompat.getDrawable(getResources(), R.drawable.pull_up, null);
            imgEx.setImageDrawable(pic);
            tvHowtoUse.setText("Đặt điện thoại ngang chân FireFit sẽ dùng cảm biến tiệm cận để có thể ghi nhận quá trình tập. Bạn hoàn toàn có thể ghi nhận thủ công sau phiên tập");
        } else if (exerciseName.equals("Chạy bộ")){
            tvTopicEx.setText("CHẠY BỘ CÙNG FIREFIT");
            tvNotUse.setVisibility(View.INVISIBLE);
            Drawable pic = ResourcesCompat.getDrawable(getResources(), R.drawable.running, null);
            imgEx.setImageDrawable(pic);
            tvHowtoUse.setText("Cầm điện thoại lúc chạy bộ để FireFit có thẻ tự động ghi nhận các thông tin luyện tập.");
        } else if (exerciseName.equals("Khác")){
            tvTopicEx.setText("BÀI TẬP THÊM CÙNG FIREFIT");
            tvNotUse.setVisibility(View.INVISIBLE);
            Drawable pic = ResourcesCompat.getDrawable(getResources(), R.drawable.exercise, null);
            imgEx.setImageDrawable(pic);
            tvHowtoUse.setText("Bạn có bài tập đặc biệt của riêng mình? FireFit sẽ giúp bạn ghi nhận phiên tập.");
        }
    }

    private void setButtonStart(){
        imgStartEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise(true);
            }
        });

        tvStartEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise(true);
            }
        });
    }

    private void startExercise(boolean isUsedSensor){

        if (exerciseName.equals("Hít đất")){
            Intent intent = new Intent(this , DoExercise.class);
            intent.putExtra("Name",exerciseName);
            intent.putExtra("isSensorUsed",isUsedSensor);
            startActivity(intent);
        }
        else if (exerciseName.equals("Gập bụng")){
            Intent intent = new Intent(this , DoExercise.class);
            intent.putExtra("Name",exerciseName);
            intent.putExtra("isSensorUsed",isUsedSensor);
            startActivity(intent);
        }
        else if (exerciseName.equals("Hít xà")){
            Intent intent = new Intent(this , DoExercise.class);
            intent.putExtra("Name",exerciseName);
            intent.putExtra("isSensorUsed",isUsedSensor);
            startActivity(intent);
        }
        else if (exerciseName.equals("Chạy bộ")){
            Intent intent = new Intent(this , MapRunActivity.class);
            startActivity(intent);
        }
        else if (exerciseName.equals("Khác")){
            Intent intent = new Intent(this , DoExercise.class);
            intent.putExtra("isSensorUsed",false);
            intent.putExtra("Name",exerciseName);
            startActivity(intent);
        }
    }

    private void setButtonNotUseSensor(){
        tvNotUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise(false);
            }
        });
    }
}