package com.example.healthtracking;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    int[] stepsCounter;
    int[] kilometers;
    int[] calories;
    TextView tvStepsCounter;
    TextView tvKilometer;
    TextView tvCalories;


    public HomeAdapter(int[] stepsCounter, int[] kilometers, int[] calories){
        this.stepsCounter = stepsCounter;
        this.kilometers = kilometers;
        this.calories = calories;
    }
    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        tvStepsCounter = (TextView) cardView.findViewById(R.id.stepsCounter);
        tvStepsCounter.setText("" + stepsCounter[position]);
        tvKilometer = (TextView) cardView.findViewById(R.id.kilometer);
        tvKilometer.setText("" + kilometers[position] + "Kilomet");
        tvCalories = (TextView) cardView.findViewById(R.id.calories);
        tvCalories.setText("" + calories[position] + "Kalo");
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
}
