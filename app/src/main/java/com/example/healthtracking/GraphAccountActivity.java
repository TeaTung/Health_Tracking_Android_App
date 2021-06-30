package com.example.healthtracking;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class GraphAccountActivity extends AppCompatActivity {
    View decorateView;
    PieChart piechart;
    float consumedCalories, releasedCalories, numberOfFireFit;
    Spinner sprFilter;
    TextView tvConsumedCalories, tvReleasedCalories, tvReceivedFireFit, tvSearch;
    ImageView imgSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_graph);

        piechart = findViewById(R.id.piechart);
        sprFilter = findViewById(R.id.sprFilter);
        tvConsumedCalories = findViewById(R.id.tvConsumedCalories);
        tvReleasedCalories = findViewById(R.id.tvReleasedCalories);
        tvReceivedFireFit = findViewById(R.id.tvReceivedFireFit);
        tvSearch = findViewById(R.id.tvSearch);
        imgSearch = findViewById(R.id.imgSearch);

        setUpSpinner();
        decorView();
        setUpPieChart();
        setOnClick();
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
    private void setUpSpinner(){
        String arr[] = {"Ngày", "Tuần", "Tháng"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,arr);
        sprFilter.setAdapter(adapter);
    }
    private void setUpPieChart(){
        consumedCalories = 60;
        releasedCalories = 40;
        numberOfFireFit = 5;

        piechart.addPieSlice(new PieModel("Consumed Calories", consumedCalories, Color.parseColor("#FE6DA8")));
        piechart.addPieSlice(new PieModel("Released Calories", releasedCalories, Color.parseColor("#56B7F1")));

        piechart.startAnimation();

        tvConsumedCalories.setText("Tiêu thụ " + consumedCalories +" calories");
        tvReleasedCalories.setText("Luyện tập " + releasedCalories + " calories");
        tvReceivedFireFit.setText("Đạt " + numberOfFireFit+ " FireFit day");
    }
    private void setOnClick(){
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                piechart.clearChart();
                search();
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                piechart.clearChart();
                search();
            }
        });
    }
    private void search(){
        String filter = sprFilter.getSelectedItem().toString();
        getData(filter);

        piechart.addPieSlice(new PieModel("Consumed Calories", consumedCalories, Color.parseColor("#FE6DA8")));
        piechart.addPieSlice(new PieModel("Released Calories", releasedCalories, Color.parseColor("#56B7F1")));

        piechart.startAnimation();

        tvConsumedCalories.setText("Tiêu thụ " + consumedCalories +" calories");
        tvReleasedCalories.setText("Luyện tập " + releasedCalories + " calories");
        tvReceivedFireFit.setText("Đạt " + numberOfFireFit+ " FireFit day");
    }
    private void getData(String filter){
        if (filter == "Ngày"){

        } else if (filter == "Tuần"){

        } else if (filter == "Tháng"){

        }
    }
}