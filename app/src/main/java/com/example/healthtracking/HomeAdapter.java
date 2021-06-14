package com.example.healthtracking;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthtracking.ClassData.OnedayofPractice;
import com.example.healthtracking.ClassData.Run;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    int[] stepsCounter;
    TextView tvStepsCounter;
    TextView tvKilometer;
    TextView tvCalories;
    ProgressBar progressBar;


    public HomeAdapter(int[] stepsCounter){
        this.stepsCounter = stepsCounter;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        double km = Math.round(stepsCounter[position] * 0.7*100)/100;
        double calo = km * 0.0625;

        progressBar = (ProgressBar)cardView.findViewById(R.id.stepProgress);
        progressBar.setMax(1000);
        progressBar.setProgress(stepsCounter[position]);
        tvStepsCounter = (TextView) cardView.findViewById(R.id.stepsCounter);
        tvStepsCounter.setText(""+stepsCounter[position]);
        tvKilometer = (TextView) cardView.findViewById(R.id.kilometer);
        tvKilometer.setText(km + " kilomet");
        tvCalories = (TextView) cardView.findViewById(R.id.calories);
        tvCalories.setText(calo + " calo");
        checkDaydataIsExist(stepsCounter[position],km,calo);
       // UpdateDataIntoDatabase(stepsCounter[position],km,calo);
    }

    @Override
    public int getItemCount() {
        return stepsCounter.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v){
            super(v);
            cardView = v;
        }
    }
    public void UpdateDataIntoDatabase(int step, double distance, double calo) {
        DatabaseReference mDatabase;
        FirebaseAuth mAuth;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser fuser = mAuth.getCurrentUser();

        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        Run run = new Run(step, distance, calo);
        mDatabase.child(fuser.getUid()).child("practice").child(date.toString()).child("run").setValue(run);
    }


    public  void checkDaydataIsExist(int step, double distance, double calo) {
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                if (snapshot.getValue() == null) {
//
//                    FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
//                            .setValue(new OnedayofPractice((new Run(0, 0, 0)), 0, 0));
//                    Run run = new Run(step, distance, calo);
//                    FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
//                            .child("run").setValue(run);
//                }
//                else
                {
                    Run run = new Run(step, distance, calo);
                    FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                            .child("run").setValue(run);
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}
