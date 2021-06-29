package com.example.healthtracking.CheckFireFitDay;

import androidx.annotation.NonNull;

import com.example.healthtracking.ClassData.Goal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class CheckFireFitDay {

    public CheckFireFitDay() {
    }

    public void CheckOneDay(String time, String time2)
    {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Goal goal = snapshot.child("profile").child("DayGoal").getValue(Goal.class);
                int ffday = snapshot.child("practice").child(time).child("firefitday").getValue(Integer.class);
                double intakecalo = goal.IntakeCalories;
                double sumexcercisecalo = goal.ExerciseCalories;
                double goalwater = goal.Water;
                double runcalo = snapshot.child("practice").child(time).child("run").child("Calories").getValue(double.class);
                double jogcalo = snapshot.child("practice").child(time).child("jog").child("Calories").getValue(double.class);
                double nutricalo = snapshot.child("practice").child(time).child("nutrition").child("Calories").getValue(double.class);
                int water  = snapshot.child("practice").child(time).child("nutrition").child("Water").getValue(Integer.class);
                double exercisecalo = snapshot.child("practice").child(time).child("exercise").child("Calories").getValue(double.class);
                if ((jogcalo + runcalo + exercisecalo >= sumexcercisecalo) && (water >= goalwater) && (nutricalo >= intakecalo)) {
                    FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(time)
                            .child("firefitday").setValue(1);
                    if (snapshot.child("practice").child(time2).getValue() != null)
                    {
                        int i = snapshot.child("profile").child("FireFitStreak").getValue(Integer.class);
                        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("profile").child("FireFitStreak")
                                .setValue(i+1);
                    }
                    else
                    {
                        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("profile").child("FireFitStreak")
                                .setValue(1);
                    }
                }
                else
                {
                    FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(time)
                            .child("firefitday").setValue(0);
                    if (ffday == 1)
                    {
                        int i = snapshot.child("profile").child("FireFitStreak").getValue(Integer.class);
                        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("profile").child("FireFitStreak")
                                .setValue(i-1);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


}
