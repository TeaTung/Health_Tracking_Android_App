package com.example.healthtracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GraphAccountActivity extends AppCompatActivity {
    View decorateView;
    PieChart piechart;
    float consumedCalories, releasedCalories;
    int numberOfFireFit;
    Spinner sprFilter;
    TextView tvConsumedCalories, tvReleasedCalories, tvReceivedFireFit, tvSearch;
    ImageView imgSearch, imgOk;
    String arr[] = {"Ngày", "Tuần", "Tháng"};
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
        imgSearch = findViewById(R.id.okBtn);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setUpSpinner();
        decorView();
        // setUpPieChart();
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,arr);
        sprFilter.setAdapter(adapter);
    }
    private void setUpPieChart(){
        getDataForDay();
    }
    private void setOnClick(){

        sprFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sprFilter.getSelectedItem().toString() == "Ngày") {
                    piechart.clearChart();
                    getDataForDay();
                }
                if (sprFilter.getSelectedItem().toString() == "Tuần") {
                    piechart.clearChart();
                    getDataForWeek();
                }
                if (sprFilter.getSelectedItem().toString() == "Tháng") {
                    piechart.clearChart();
                    getDataForMonth();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    private void search(){

        piechart.addPieSlice(new PieModel("Consumed Calories", consumedCalories, Color.parseColor("#FE6DA8")));
        piechart.addPieSlice(new PieModel("Released Calories", releasedCalories, Color.parseColor("#56B7F1")));

        piechart.startAnimation();

        tvConsumedCalories.setText("Tiêu thụ " + Math.round(consumedCalories) +" calories");
        tvReleasedCalories.setText("Luyện tập " + Math.round(releasedCalories) + " calories");
        tvReceivedFireFit.setText("Đạt " + numberOfFireFit+ " FireFit day");
    }
    private void getData(String filter){
        if (filter == "Ngày"){

        } else if (filter == "Tuần"){

        } else if (filter == "Tháng"){

        }
    }

    public void getDataForDay()
    {
        long millis = System.currentTimeMillis() ;
        java.sql.Date date = new java.sql.Date(millis);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        int ffday = snapshot.child("firefitday").getValue(Integer.class);
                        double runcalo = snapshot.child("run").child("Calories").getValue(double.class);
                        double jogcalo = snapshot.child("jog").child("Calories").getValue(double.class);
                        double nutricalo = snapshot.child("nutrition").child("Calories").getValue(double.class);
                        double exercisecalo = snapshot.child("exercise").child("Calories").getValue(double.class);
                        consumedCalories = Float.parseFloat(""+nutricalo);
                        double sum = runcalo + jogcalo+exercisecalo;
                        releasedCalories = Float.parseFloat(""+sum);
                        numberOfFireFit = ffday;
                        search();

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    private void getDataForWeek() {
        Calendar cal = Calendar.getInstance();
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        long milis = System.currentTimeMillis();

        List<String> listDate = new ArrayList<>();
        if (dayofweek == 1) listDate = getStringDate(milis, 7);
        else listDate = getStringDate(milis, dayofweek - 1);
        int N = listDate.size();
        List<String> finalListDate = listDate;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        int ffday =0;
                        double runcalo = 0;
                        double jogcalo = 0;
                        double nutricalo = 0;
                        double exercisecalo = 0;
                        for (int i = 0; i< N-1; i++)
                        {
                            if (snapshot.child(finalListDate.get(i)).getValue() != null)
                            {
                                ffday += snapshot.child(finalListDate.get(i)).child("firefitday").getValue(Integer.class);
                                runcalo += snapshot.child(finalListDate.get(i)).child("run").child("Calories").getValue(double.class);
                                jogcalo += snapshot.child(finalListDate.get(i)).child("jog").child("Calories").getValue(double.class);
                                nutricalo += snapshot.child(finalListDate.get(i)).child("nutrition").child("Calories").getValue(double.class);
                                exercisecalo += snapshot.child(finalListDate.get(i)).child("exercise").child("Calories").getValue(double.class);
                            }
                        }
                        consumedCalories = Float.parseFloat(""+nutricalo);
                        double sum = runcalo + jogcalo+exercisecalo;
                        releasedCalories = Float.parseFloat(""+sum);
                        numberOfFireFit = ffday;
                        search();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


    }

    private void getDataForMonth() {
        Calendar cal = Calendar.getInstance();
        int dayofmonth = cal.get(Calendar.DAY_OF_MONTH);
        long milis = System.currentTimeMillis();

        List<String> listDate = new ArrayList<>();
        listDate = getStringDate(milis, dayofmonth );
        int N = listDate.size();
        List<String> finalListDate = listDate;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        int ffday =0;
                        double runcalo = 0;
                        double jogcalo = 0;
                        double nutricalo = 0;
                        double exercisecalo = 0;
                        for (int i = 0; i< N-1; i++)
                        {
                            if (snapshot.child(finalListDate.get(i)).getValue() != null)
                            {
                                ffday += snapshot.child(finalListDate.get(i)).child("firefitday").getValue(Integer.class);
                                runcalo += snapshot.child(finalListDate.get(i)).child("run").child("Calories").getValue(double.class);
                                jogcalo += snapshot.child(finalListDate.get(i)).child("jog").child("Calories").getValue(double.class);
                                nutricalo += snapshot.child(finalListDate.get(i)).child("nutrition").child("Calories").getValue(double.class);
                                exercisecalo += snapshot.child(finalListDate.get(i)).child("exercise").child("Calories").getValue(double.class);
                            }
                        }
                        consumedCalories = Float.parseFloat(""+nutricalo);
                        double sum = runcalo + jogcalo+exercisecalo;
                        releasedCalories = Float.parseFloat(""+sum);
                        numberOfFireFit = ffday;
                        search();

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    public List<String> getStringDate(long currentdate, int amount)
    {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < amount; i++)
        {
            long milis = currentdate - i*24*60*60*1000;
            java.sql.Date date = new Date(milis);
            list.add(date.toString());
        }
        return list;
    }
}