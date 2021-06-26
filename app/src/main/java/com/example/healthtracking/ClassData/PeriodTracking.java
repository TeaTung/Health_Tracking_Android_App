package com.example.healthtracking.ClassData;

import android.widget.CheckBox;

public class PeriodTracking {
    public long RecentDate;
    public long NextDate;
    public int Period;
    public long Date1, Date2, Date3, Date4, Date5, Date6;
    public int IsAuto, Average;


    public PeriodTracking() {
        RecentDate = 0;
        NextDate = 0;
        Period = 28;
        Date1 = 0;
        Date2 = 0;
        Date3 = 0;
        Date4 = 0;
        Date5 = 0;
        Date6 = 0;
        IsAuto = 1;
        Average = 1;
    }

    public PeriodTracking(long recentDate, long nextDate, int period, long date1, long date2, long date3, long date4, long date5, long date6, int isAuto, int average) {
        RecentDate = recentDate;
        NextDate = nextDate;
        Period = period;
        Date1 = date1;
        Date2 = date2;
        Date3 = date3;
        Date4 = date4;
        Date5 = date5;
        Date6 = date6;
        IsAuto = isAuto;
        Average = average;
    }
}
