package com.example.healthtracking;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthtracking.ClassData.Run;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        UpdateDataIntoDatabase(stepsCounter[position],km,calo);
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

}
