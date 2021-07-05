package com.example.healthtracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthtracking.ClassData.Jog;
import com.example.healthtracking.ClassData.PeriodTracking;
import com.example.healthtracking.ClassData.Run;
import com.google.android.material.datepicker.MaterialCalendar;
import com.shawnlin.numberpicker.NumberPicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.text.ParseException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import sun.bob.mcalendarview.MCalendarView;

import static java.lang.Integer.max;

public class TrackingPeriodActivity extends AppCompatActivity {
    TextView textViewRecentDate, textViewNextDate, textViewRecordDate, textViewRecord;
    NumberPicker numberPicker;
    CalendarView calendarView;
    CheckBox checkBox;
    ImageButton imageRecord;
    View decorateView;
    Button btnExitPeriod, btnClearData;
    long recentDate, nextDate, recorDate;
    int period;
    long[] listDate ;
    int isAuto, average;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.period_tracking);
        AnhXa();
        LoadDataForFirst();
        Event();
        decorView();
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
    public  void AnhXa()
    {
        btnClearData = (Button) findViewById(R.id.btnClearData);
        btnExitPeriod = (Button) findViewById(R.id.btnExitPeriod);
        textViewRecentDate = (TextView) findViewById(R.id.textViewRecentDate);
        textViewNextDate = (TextView) findViewById(R.id.textViewNextDate);
        textViewRecordDate = (TextView) findViewById(R.id.textViewDateRecord);
        textViewRecord = (TextView) findViewById(R.id.tvRecord);
        imageRecord = (ImageButton) findViewById(R.id.imageRecord);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        numberPicker = (NumberPicker) findViewById(R.id.number_picker);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
    }

    public  void Event()
    {
        btnClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (month +1 < 10) textViewRecordDate.setText(dayOfMonth+"/0"+(month+1)+"/"+year);
                else textViewRecordDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    numberPicker.setValue(period);
                    numberPicker.setScrollerEnabled(false);
                    isAuto = 1;
                }
                else
                {
                    isAuto = 0;
                    numberPicker.setScrollerEnabled(true);
                }
            }
        });
        btnExitPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordPeriod();
            }
        });

        textViewRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordPeriod();
            }
        });
    }
    public void LoadDataForFirst()
    {
        long millis = calendarView.getDate() ;
        java.sql.Date date = new java.sql.Date(millis);
        listDate = new long[6];
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("profile").child("DetailPeriod")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        recentDate = snapshot.child("RecentDate").getValue(long.class);
                        nextDate = snapshot.child("NextDate").getValue(long.class);
                        listDate[0] = (snapshot.child("Date1").getValue(long.class));
                        listDate[1] = (snapshot.child("Date2").getValue(long.class));
                        listDate[2] = (snapshot.child("Date3").getValue(long.class));
                        listDate[3] = (snapshot.child("Date4").getValue(long.class));
                        listDate[4] = (snapshot.child("Date5").getValue(long.class));
                        listDate[5] = (snapshot.child("Date6").getValue(long.class));
                        period =  snapshot.child("Period").getValue(Integer.class);
                        isAuto = snapshot.child("IsAuto").getValue(Integer.class);
                        average = snapshot.child("Average").getValue(Integer.class);

                        if (recentDate == 0) textViewRecentDate.setText("Hiện chưa có dữ liệu");
                        else textViewRecentDate.setText(FormatDate(new java.sql.Date(recentDate).toString()));

                        if (nextDate == 0) textViewNextDate.setText("Hiện chưa có dữ liệu");
                        else textViewNextDate.setText(FormatDate(new java.sql.Date(nextDate).toString()));

                        textViewRecordDate.setText(FormatDate(date.toString()));
                        numberPicker.setValue(period);
                        if (isAuto == 1) {
                            checkBox.setChecked(true);
                            numberPicker.setValue(period);
                            numberPicker.setScrollerEnabled(false);
                        }
                        else
                        {
                            checkBox.setChecked(false);
                            numberPicker.setScrollerEnabled(true);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    public  String FormatDate(String date)
    {
        String[] result = date.split("-");

        return result[2]+"/"+result[1]+"/"+result[0];
    }

    public  void RecordPeriod()
    {
        period = numberPicker.getValue();
        int index = FindEmptyDate();
        if (index != 0)
        {
            if (index == 1)
            {
                listDate[0] = ConvertDateToMilisecond(textViewRecordDate.getText().toString());
                recentDate = listDate[0];
                long x = (recentDate/100000) + period*24*6*6;
                nextDate = x*100000;
                PeriodTracking periodTracking = new PeriodTracking(recentDate, nextDate, period, listDate[0], listDate[1],
                        listDate[2], listDate[3], listDate[4], listDate[5], isAuto, average);
                UpdatePeriod(periodTracking);
            }
            else
            if (index >= 2)
            {

                listDate[index-1] = ConvertDateToMilisecond(textViewRecordDate.getText().toString());
                recentDate = listDate[index-1];
                period = GetAverage();
                long x = (recentDate/100000) + period*24*6*6;
                nextDate = x*100000;
                PeriodTracking periodTracking = new PeriodTracking(recentDate, nextDate, period, listDate[0], listDate[1],
                        listDate[2], listDate[3], listDate[4], listDate[5], isAuto, average);

                UpdatePeriod(periodTracking);
            }
            else if (index ==0)
            {
                listDate[0]= listDate[1];
                listDate[1]= listDate[2];
                listDate[2]= listDate[3];
                listDate[3]= listDate[4];
                listDate[4]= listDate[5];
                listDate[5] = ConvertDateToMilisecond(textViewRecordDate.getText().toString());
                recentDate = listDate[5];
                period = GetAverage();
                long x = (recentDate/100000) + period*24*6*6;
                nextDate = x*100000;
                PeriodTracking periodTracking = new PeriodTracking(recentDate, nextDate, period, listDate[0], listDate[1],
                        listDate[2], listDate[3], listDate[4], listDate[5], isAuto, average);
                UpdatePeriod(periodTracking);

            }
        }
        LoadDataForFirst();
    }

    public int GetAverage()
    {
        int sum = 0;
        double avg = 0;
        int index = FindEmptyDate();
        if (index == 0) index=6;
        else index = index -1;
        for (int i = 1; i < index ; i++)
        {
            int x = Integer.parseInt(""+listDate[i]/100000);
            int y = Integer.parseInt(""+listDate[i-1]/100000);
            int z = (x-y)/864;
            sum = sum +z;
            avg = avg+ 1;
        }
        return Integer.parseInt(""+Math.round( sum*1.0/avg));
    }

    public long ConvertDateToMilisecond(String Sdate) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date ;
        try {
            date = new Date(format.parse(Sdate).getTime());
            return  date.getTime();
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public int FindEmptyDate()
    {
        for (int i = 0; i < 6; i++ )
            if (listDate[i] == 0) return i+1;
        return 0;
    }

    public  void UpdatePeriod(PeriodTracking periodTracking)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("profile").child("DetailPeriod").setValue(periodTracking);
    }


    public  void resetData()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("profile").child("DetailPeriod").setValue(new PeriodTracking());
        LoadDataForFirst();
    }


}