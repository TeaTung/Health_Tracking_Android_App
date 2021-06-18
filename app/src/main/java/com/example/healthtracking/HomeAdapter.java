package com.example.healthtracking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthtracking.CardView.Exercises;
import com.example.healthtracking.CardView.Food;
import com.example.healthtracking.CardView.FunFact;
import com.example.healthtracking.CardView.Item;
import com.example.healthtracking.CardView.PersonalInformation;
import com.example.healthtracking.ClassData.Run;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Item> items;

    public HomeAdapter(List<Item> items){
        this.items = items;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new PersonalMaleInformationHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.male_personal_info_card_view,
                            parent,
                            false
                    )
            );
        } else if (viewType == 1) {
            return new PersonalFemaleInformationHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.female_personal_info_card_view,
                            parent,
                            false
                    )
            );
        }
        else if (viewType == 2) {
            return new ExerciseViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.exercise_card_view,
                            parent,
                            false
                    )
            );
        } else if (viewType == 3) {
            return new FoodViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.food_card_view,
                            parent,
                            false
                    )
            );
        } else if (viewType == 4) {
            return new FunFactViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.fun_fact_card_view,
                            parent,
                            false
                    )
            );
        }
        return null;
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            PersonalInformation personalMaleInformation = (PersonalInformation)items.get(position).getObject();
            ((PersonalMaleInformationHolder)holder).setPersonalInformationCard(personalMaleInformation);
        }

        else if (getItemViewType(position) == 1) {
            PersonalInformation personalFemaleInformation = (PersonalInformation)items.get(position).getObject();
            ((PersonalFemaleInformationHolder)holder).setPersonalInformationCard(personalFemaleInformation);
        }

        else if (getItemViewType(position) == 2) {
            Exercises exercises = (Exercises)items.get(position).getObject();
            ((ExerciseViewHolder)holder).setExerciseCard(exercises);
        }

        else if (getItemViewType(position) == 3) {
            Food food = (Food)items.get(position).getObject();
            ((FoodViewHolder)holder).setFoodCard(food);
        }

        else if (getItemViewType(position) == 4) {
            FunFact funFact = (FunFact)items.get(position).getObject();
            ((FunFactViewHolder)holder).setFundFactCard(funFact);
        }
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder{
        private TextView tvStepCounter, tvCaloriesInEx, tvDistanceInEx;
        private ProgressBar progressBarExercise;

        public ExerciseViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvStepCounter = itemView.findViewById(R.id.tvStepInEx);
            tvCaloriesInEx = itemView.findViewById(R.id.tvCaloriesInEx);
            tvDistanceInEx = itemView.findViewById(R.id.tvDistanceInEx);
            progressBarExercise = itemView.findViewById(R.id.pgbStep);
        }
        void setExerciseCard(Exercises exercise){
            tvStepCounter.setText(exercise.getStepsCounter() + "");
            tvCaloriesInEx.setText(exercise.getCalories()+ "");
            tvDistanceInEx.setText(exercise.getDistances()+ "");
            progressBarExercise = exercise.getProgressBar();
        }
    }
    static class FoodViewHolder extends RecyclerView.ViewHolder{
        private TextView tvCaloriesInFood, tvLit,tvFoodCounter;
        private ProgressBar progressBarFood;
        public FoodViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvCaloriesInFood = itemView.findViewById(R.id.tvCaloriesInFood);
            tvLit = itemView.findViewById(R.id.tvLit);
            tvFoodCounter = itemView.findViewById(R.id.tvFoodCounter);
            progressBarFood = itemView.findViewById(R.id.pgbFood);
        }
        void setFoodCard(Food food){
            tvCaloriesInFood.setText("" + food.getCalories());
            tvLit.setText("" + food.getLit());
            tvFoodCounter.setText("" + food.getCalories());
            progressBarFood = food.getProgressBar();
        }
    }
    static class FunFactViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle, tvQuote;
        public FunFactViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvQuote = itemView.findViewById(R.id.tvQuote);
        }
        void setFundFactCard(FunFact funFact) {
//            tvTitle.setText(funFact.getTitle());
//            tvQuote.setText(funFact.getQuote());
        }
    }
    static class PersonalMaleInformationHolder extends RecyclerView.ViewHolder{

        private TextView tvHeightInMale, tvWeighInMale;

        public PersonalMaleInformationHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvHeightInMale = itemView.findViewById(R.id.tvHeighInMale);
            tvWeighInMale = itemView.findViewById(R.id.tvWeighInMale);
        }
        void setPersonalInformationCard(PersonalInformation personalInformation) {
            tvHeightInMale.setText("" + personalInformation.getHeight());
            tvWeighInMale.setText("" + personalInformation.getWeight());
        }
    }
    static class PersonalFemaleInformationHolder extends RecyclerView.ViewHolder{
        private TextView tvHeightInFemale, tvWeighInFemale, tvDayInFemale;
        public PersonalFemaleInformationHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvHeightInFemale = itemView.findViewById(R.id.tvHeighInFemale);
            tvWeighInFemale = itemView.findViewById(R.id.tvWeighInFemale);
            tvDayInFemale = itemView.findViewById(R.id.tvDayInFemale);
        }
        void setPersonalInformationCard(PersonalInformation personalInformation) {
            tvHeightInFemale.setText("" + personalInformation.getHeight());
            tvWeighInFemale.setText("" + personalInformation.getWeight());
            tvDayInFemale.setText("" + personalInformation.getDay());
        }
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
