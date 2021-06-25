package com.example.healthtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

public class InformationAccountActivity extends AppCompatActivity {
    AutoCompleteTextView atvHeight ,atvWeight;
    ImageView imgRecordInAccount;
    TextView tvRecordInAccount, tvBMI;
    View decorateView;

    double height, weight ,BMI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);

        atvHeight = findViewById(R.id.atvHeight);
        atvWeight = findViewById(R.id.atvWeight);
        imgRecordInAccount = findViewById(R.id.imgRecordInAccount);
        tvRecordInAccount = findViewById(R.id.tvRecordInAccount);
        tvBMI = findViewById(R.id.tvBMI);

        decorView();
        setOnClick();
        setBMI();
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
    private void setBMI(){
        height = 175.0;
        weight = 65.0;
        BMI = Math.round((weight / ((height/100)*2)*10)/10) ;

        if (BMI < 18.5){
            tvBMI.setText("Chỉ số BMI của bạn là: " + BMI + " - Vóc dáng hơi gầy");
        } else if (BMI >= 18.5 && BMI < 23.0){
            tvBMI.setText("Chỉ số BMI của bạn là: " + BMI + " - Vóc dáng cân đối");
        } else if (BMI >= 23 && BMI < 25){
            tvBMI.setText("Chỉ số BMI của bạn là: " + BMI + " - Vóc dáng hơi thừa cân");
        } else if (BMI >= 25 && BMI < 30){
            tvBMI.setText("Chỉ số BMI của bạn là: " + BMI + " - Vóc dáng thừa cân");
        } else {
            tvBMI.setText("Chỉ số BMI của bạn là: " + BMI + " - Bạn đang mắ bệnh béo phì");
        }
    }
    private void setOnClick(){
        imgRecordInAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonRecord();
            }
        });
        tvRecordInAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonRecord();
            }
        });
    }
    private void setButtonRecord(){
        if (!atvWeight.getText().toString().equals("") && !atvHeight.getText().toString().equals("")){

        }
    }
}