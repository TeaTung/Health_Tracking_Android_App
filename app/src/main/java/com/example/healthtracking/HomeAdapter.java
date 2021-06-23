package com.example.healthtracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Item> items;
    private  int stepcout;

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
          //  stepcout = exercises.getStepsCounter();
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
            tvCaloriesInEx.setText((int)exercise.getCalories()+ " Kalos");
            if (exercise.getDistances() < 1000)
            {
                tvDistanceInEx.setText((exercise.getDistances()) + " m");
            }
            else tvDistanceInEx.setText(Math.round(exercise.getDistances()/10)/100 + " km");
            progressBarExercise.setMax(10000);
            progressBarExercise.setProgress(exercise.getStepsCounter());


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
            tvCaloriesInFood.setText("" + food.getCalories()+" Calories");
            tvLit.setText("" + food.getLit()+" ml");
            tvFoodCounter.setText("" + food.getCalories());
            progressBarFood.setMax(2000);
            progressBarFood.setProgress((int)Math.round(food.getCalories()));
        }
    }
    static class FunFactViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle, tvQuote;
        private ShapeableImageView shapeableImageView;
        String TextFileURL, TextHolder2="",TextHolder="";
        BufferedReader bufferReader ;
        URL url ;
        public FunFactViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvQuote = itemView.findViewById(R.id.tvQuote);
            shapeableImageView = itemView.findViewById(R.id.pictureFunFact);
        }
        void setFundFactCard(FunFact funFact) {
//            tvTitle.setText(funFact.getTitle());
//            tvQuote.setText(funFact.getQuote());
            FirebaseDatabase.getInstance().getReference().child("Picture").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    String x = snapshot.child("text").getValue(String.class);
                    TextFileURL = x;
                    Picasso.get().load(snapshot.child("image").getValue(String.class)).into(shapeableImageView);
                    new GetNotePadFileFromServer().execute();


                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }

        public class GetNotePadFileFromServer extends AsyncTask<Void, Void, Void> {
            @Override protected Void doInBackground(Void... params) {
                try { url = new URL(TextFileURL);
                    bufferReader = new BufferedReader(new InputStreamReader(url.openStream()));
                    while ((TextHolder2 = bufferReader.readLine()) != null)
                    {
                        TextHolder += TextHolder2; } bufferReader.close();
                } catch (MalformedURLException malformedURLException) {

                    malformedURLException.printStackTrace();
                    TextHolder = malformedURLException.toString();
                } catch (IOException iOException) {

                    iOException.printStackTrace();
                    TextHolder = iOException.toString();
                } return null;
            }
            @Override protected void onPostExecute(Void finalTextHolder) {
                tvQuote.setText(TextHolder);
                super.onPostExecute(finalTextHolder);
            }
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


}
