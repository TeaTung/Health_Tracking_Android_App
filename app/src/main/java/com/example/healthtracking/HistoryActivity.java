package com.example.healthtracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.healthtracking.ClassData.Jog;
import com.example.healthtracking.ClassData.Run;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class HistoryActivity extends AppCompatActivity {
    TextView textViewKm, textViewKaloUse, textViewKaloExercise, textViewStepCount, textViewSpeed, textViewTime;
    CalendarView calendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        AnhXa();
        InitData();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (month +1 < 10) LoadData(year+"-0"+(month+1)+"-"+dayOfMonth);
                else LoadData(year+"-"+(month+1)+"-"+dayOfMonth);
            }
        });

    }

    private void InitData() {
        long millis = calendarView.getDate();
        java.sql.Date date = new java.sql.Date(millis);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null)
                        {
                            Run run = snapshot.child("run").getValue(Run.class);
                            Jog jog = snapshot.child("jog").getValue(Jog.class);
                            textViewKm.setText(ConvertMetToKilomet(run.Distance+jog.Distance));
                            textViewTime.setText(ConvertTimeToString(jog.Time) + " h");
                            textViewStepCount.setText((run.StepCount+jog.StepCount) + " bước");
                            textViewKaloExercise.setText((run.Calories + jog.Calories) + " kalos" );

                        }
                        else
                        {
                            textViewKm.setText(0 + " km");
                            textViewTime.setText(0 +" h");
                            textViewStepCount.setText(0+ " bước");
                            textViewKaloExercise.setText(0+" kalos" );
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

    }

    private void LoadData(String date) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null)
                        {
                            Run run = snapshot.child("run").getValue(Run.class);
                            Jog jog = snapshot.child("jog").getValue(Jog.class);
                            textViewKm.setText(ConvertMetToKilomet(run.Distance+jog.Distance));
                            textViewTime.setText(ConvertTimeToString(jog.Time) + " h");
                            textViewStepCount.setText((run.StepCount+jog.StepCount) + " bước");
                            textViewKaloExercise.setText((run.Calories + jog.Calories) + " kalos" );

                        }
                        else
                        {
                            textViewKm.setText(0 + " km");
                            textViewTime.setText(0 +" h");
                            textViewStepCount.setText(0+ " bước");
                            textViewKaloExercise.setText(0+" kalos" );
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

    }


    public  void AnhXa()
    {
        textViewKm = (TextView) findViewById(R.id.textViewKm);
        textViewKaloUse = (TextView) findViewById(R.id.textViewKaloUse);
        textViewKaloExercise = (TextView) findViewById(R.id.textViewKaloExercise);
        textViewSpeed = (TextView) findViewById(R.id.textViewSpeed);
        textViewStepCount = (TextView) findViewById(R.id.textViewCalo);
        textViewTime = (TextView) findViewById(R.id.textViewWater);
        calendarView = (CalendarView) findViewById(R.id.calendarView);


    }

    public  String ConvertMetToKilomet(double i)
    {
        if (i<1000) return i+" m";
        else return  Math.round((i*1.0/1000)*100)/100.0 +" km";

    }
    public  String ConvertTimeToString(int i)
    {
        double x = (i*1.0)/3600;
        if (Math.round(x) == i/3600) return ""+ i/3600;
        return ""+Math.round(x*100.0)/100.0;
    }
}