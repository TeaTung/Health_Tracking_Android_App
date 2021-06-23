package com.example.healthtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class InstructionCounterPeriodActivity extends AppCompatActivity {
    ImageView imgStartPeriod;
    TextView tvStartPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.period_counter_instruction);

        imgStartPeriod = (ImageView) findViewById(R.id.imgStartPeriod);
        tvStartPeriod = (TextView) findViewById(R.id.tvStartPeriod);

        setClick();
    }

    private void setClick(){
        imgStartPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartPeriod();
            }
        });

        tvStartPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartPeriod();
            }
        });
    }
    private void setStartPeriod(){
        Intent intent = new Intent(this, TrackingPeriodActivity.class);
        startActivity(intent);
    }
}