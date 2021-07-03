package com.example.healthtracking;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthtracking.CheckFireFitDay.CheckFireFitDay;
import com.example.healthtracking.ClassData.DetailNutrition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodMale extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MaterialDayPicker materialDayPicker;
    TextView textViewDate, textViewName, textViewWater, textViewCalo
            , textViewDayKN, tvWaterML, tvRecordWater
            , tvRecordFood, textViewStreak, textViewUnitDayKn;
    List<MaterialDayPicker.Weekday> allWeekdays;
    MaterialDayPicker.Weekday currentday;

    ImageView imageFood, imgWater, imgPlus, imgMinus ,imgRecordWater, imgRecordFood
            , imgPeriod, imgHistory;
    List<String> listNameFood,listUnitFood;
    List<Double> listCaloriesFood;
    ConstraintLayout cltFood, cltWater;
    AutoCompleteTextView tvFoodName;
    EditText tvUnit,tvFoodKalories;
    long nextDate;

    double calories, caloriesOne, currentCalories;
    String unitFood;
    int drinkingWater;
    int currentDrinkWater;

    public FoodMale() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FoodFragment newInstance(String param1, String param2) {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_male, container, false);

        materialDayPicker = (MaterialDayPicker) view.findViewById(R.id.day_picker);
        textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        textViewName = (TextView) view.findViewById(R.id.textViewName);
        textViewWater = (TextView) view.findViewById(R.id.textViewWater);
        textViewUnitDayKn = (TextView) view.findViewById(R.id.tvUnitDayKn);
        textViewCalo = (TextView) view.findViewById(R.id.textViewCalo);
        textViewStreak = (TextView) view.findViewById(R.id.textViewStreak);
        imageFood = (ImageView) view.findViewById(R.id.imageFood);
        cltFood = (ConstraintLayout) view.findViewById(R.id.cltFood);
        cltWater = (ConstraintLayout) view.findViewById(R.id.cltWater);
        imgWater = (ImageView) view.findViewById(R.id.imgWater);
        imgPlus = (ImageView) view.findViewById(R.id.imgPlus);
        imgMinus = (ImageView) view.findViewById(R.id.imgMinus);
        tvWaterML = (TextView) view.findViewById(R.id.tvWaterML);
        imgRecordWater = (ImageView) view.findViewById(R.id.imgRecordWater);
        tvRecordWater = (TextView) view.findViewById(R.id.tvRecordWater);
        tvFoodName = (AutoCompleteTextView) view.findViewById(R.id.tvFoodName);
        tvUnit = (EditText) view.findViewById(R.id.tvUnit);
        tvFoodKalories = (EditText) view.findViewById(R.id.tvFoodKalories);
        tvRecordFood = (TextView) view.findViewById(R.id.tvRecordFood);
        imgRecordFood = (ImageView) view.findViewById(R.id.imgRecordFood);
        imgHistory = (ImageView) view.findViewById(R.id.historyBtn2);
        imgHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
            }
        });
        createListFood();
        Loaddata();
        setDayPicker();
        Event();

        return view;
    }

    public  void Event() {
        imageFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cltFood.setVisibility(View.VISIBLE);
                cltWater.setVisibility(View.INVISIBLE);
            }
        });
        imgWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cltWater.setVisibility(View.VISIBLE);
                cltFood.setVisibility(View.INVISIBLE);
            }
        });
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drinkingWater += 50;
                tvWaterML.setText(drinkingWater + "ml");
            }
        });
        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drinkingWater != 0){
                    drinkingWater -= 50;
                    tvWaterML.setText(drinkingWater + "ml");
                }
            }
        });
        imgRecordWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { recordDataWater();
            }
        });
        tvRecordWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordDataWater();
            }
        });
        imgRecordFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordDataFood();
            }
        });
        tvRecordFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordDataFood();
            }
        });
        tvUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    calories = caloriesOne * Integer.parseInt(tvUnit.getText().toString());
                    tvFoodKalories.setText(calories+ " Calories");
                } catch (NumberFormatException e)
                {
                    tvFoodKalories.setText("");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //// load du lieu  fragment khi moi vao
    public void Loaddata() {
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        textViewDate.setText(FormatDate(date.toString()));
        //// set profile name
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String x = snapshot.child("Name").getValue(String.class);
                textViewName.setText(x);
                nextDate = snapshot.child("DetailPeriod").child("NextDate").getValue(long.class);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    public void setDayPicker() {

        MaterialDayPicker.Weekday[] mWeekday = new MaterialDayPicker.Weekday[1];
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        materialDayPicker.setFirstDayOfWeek(MaterialDayPicker.Weekday.MONDAY);
        setSelectDayPicker(i);

        mWeekday[0] = materialDayPicker.getSelectedDays().get(0);
        materialDayPicker.setDaySelectionChangedListener(new MaterialDayPicker.DaySelectionChangedListener() {
            @Override
            public void onDaySelectionChanged(@NonNull List<MaterialDayPicker.Weekday> selectedDays) {
                if (selectedDays.size() > 0) {
                    mWeekday[0] = selectedDays.get(0);
                } else {
                    materialDayPicker.setSelectedDays(mWeekday[0]);

                }
                LoadDataForSelectedDay(materialDayPicker.getSelectedDays().get(0));

            }
        });
    }
    public void setSelectDayPicker(int i) {
        switch (i) {
            case 2:
                materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.MONDAY);
                break;
            case 3:
                materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.TUESDAY);
                break;
            case 4:
                materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.WEDNESDAY);
                break;
            case 5:
                materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.THURSDAY);
                break;
            case 6:
                materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.FRIDAY);
                break;
            case 7:
                materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.SATURDAY);
                break;
            case 1:
                materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.SUNDAY);
                break;


        }
        currentday = materialDayPicker.getSelectedDays().get(0);
        setEnableDayPicker(materialDayPicker.getSelectedDays().get(0), materialDayPicker.getFirstDayOfWeek());
        LoadDataForSelectedDay(materialDayPicker.getSelectedDays().get(0));

    }
    public void setEnableDayPicker(MaterialDayPicker.Weekday currenttday, MaterialDayPicker.Weekday firstday) {
        allWeekdays = new ArrayList<>();
        for (MaterialDayPicker.Weekday x : MaterialDayPicker.Weekday.values()) {
            allWeekdays.add(x);
        }
        for (MaterialDayPicker.Weekday x : MaterialDayPicker.Weekday.values()) {
            allWeekdays.add(x);
        }

        for (int i = 0; i < allWeekdays.size(); i++) {
            if (allWeekdays.get(i) == currenttday) {
                for (int j = i + 1; j < allWeekdays.size(); j++) {
                    if (allWeekdays.get(j) != firstday) {
                        materialDayPicker.setDayEnabled(allWeekdays.get(j), false);
                    } else break;
                }
                break;

            }
        }

    }
    public void LoadDataForSelectedDay(MaterialDayPicker.Weekday selectedday) {

        for (int i = allWeekdays.size() - 1; i >= 0; i--) {
            if (allWeekdays.get(i) == currentday) {
                for (int j = i; j >= 0; j--)
                    if (selectedday == allWeekdays.get(j)) {
                        long millis = System.currentTimeMillis() - (i - j) * 3600 * 24 * 1000;
                        long currentmillis = System.currentTimeMillis();
                        java.sql.Date date = new java.sql.Date(millis);
                        java.sql.Date currentdate = new java.sql.Date(currentmillis);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase.getInstance().getReference().child(user.getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                        //  OnedayofPractice onedayofPractice = snapshot.getValue(OnedayofPractice.class);
                                        if (snapshot.child("practice").child(date.toString()).getValue() != null)
                                        {
                                            if (date.toString().equals(currentdate.toString())) {
                                                currentDrinkWater = snapshot.child("practice").child(date.toString()).child("nutrition").child("Water").getValue(Integer.class);
                                                currentCalories = snapshot.child("practice").child(date.toString()).child("nutrition").child("Calories").getValue(double.class);
                                            }
                                            textViewWater.setText(""+snapshot.child("practice").child(date.toString()).child("nutrition").child("Water").getValue(Integer.class));
                                            int x = (int) Math.round(snapshot.child("practice").child(date.toString()).child("nutrition").child("Calories").getValue(double.class));
                                            textViewCalo.setText(""+x);
                                            int streak = snapshot.child("profile").child("FireFitStreak").getValue(Integer.class);
                                            textViewStreak.setText("Chuỗi "+streak+" Firefit days.");
                                        }
                                        else

                                        {
                                            textViewWater.setText("0");
                                            textViewCalo.setText("0");
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                        break;
                    }
                break;
            }
        }
    }
    private void createListFood(){
        listNameFood = new ArrayList<>();
        listUnitFood = new ArrayList<>();
        listCaloriesFood = new ArrayList<>();

        ArrayAdapter<String> adapterFood = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,listNameFood);
        tvFoodName.setAdapter(adapterFood);

        FirebaseDatabase.getInstance().getReference().child("Food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren())
                {
                    listNameFood.add((item.child("NameFood").getValue(String.class)).toString());
                    listUnitFood.add(item.child("UnitFood").getValue(String.class));
                    listCaloriesFood.add(item.child("Calories").getValue(double.class));
                }
                ArrayAdapter<String> adapterFood = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,listNameFood);
                adapterFood.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        tvFoodName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvUnit.setText("");
                tvFoodKalories.setText("");
                position = listNameFood.indexOf(tvFoodName.getText().toString());
                caloriesOne = listCaloriesFood.get(position);
                tvUnit.setHint(listUnitFood.get(position));
                unitFood = listUnitFood.get(position);
                tvFoodKalories.setHint(caloriesOne + " Calories/1 "+listUnitFood.get(position));
            }
        });
    }
    private void recordDataWater(){
        currentDrinkWater += drinkingWater;
        long millis = System.currentTimeMillis() ;
        long millis2 = System.currentTimeMillis() - 24*60*60*1000 ;
        java.sql.Date date = new java.sql.Date(millis);
        java.sql.Date date2 = new java.sql.Date(millis2);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                .child("nutrition").child("Water").setValue(currentDrinkWater);
        CheckFireFitDay checkFireFitDay = new CheckFireFitDay();
        checkFireFitDay.CheckOneDay(date.toString(), date2.toString());
        setDayPicker();
    }



    private void recordDataFood(){

        DetailNutrition detailNutrition = new DetailNutrition(tvFoodName.getText().toString(),tvUnit.getText().toString()+" "+unitFood,calories);
        long millis = System.currentTimeMillis() ;
        long millis2 = System.currentTimeMillis() - 24*60*60*1000 ;
        java.sql.Date date = new java.sql.Date(millis);
        java.sql.Date date2 = new java.sql.Date(millis2);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                .child("nutrition").child("detail").push().setValue(detailNutrition);
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                .child("nutrition").child("Calories").setValue(currentCalories + calories);
        CheckFireFitDay checkFireFitDay = new CheckFireFitDay();
        checkFireFitDay.CheckOneDay(date.toString(), date2.toString());
        setDayPicker();

    }

    public int setDatePeriod()
    {
        if (nextDate != 0 ) {
            long milis = System.currentTimeMillis();
            String sdate = (new Date(milis)).toString();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date;

            try {
                date = new Date(format.parse(sdate).getTime());
                int x = Integer.parseInt("" + nextDate / 100000);
                int y = Integer.parseInt("" + date.getTime() / 100000);
                int z = (x - y) / 864;
                return z;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 10000;
    }

    public  String FormatDate(String date)
    {
        String[] result = date.split("-");

        return result[2]+"/"+result[1]+"/"+result[0];
    }
}