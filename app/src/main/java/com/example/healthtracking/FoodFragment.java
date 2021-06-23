package com.example.healthtracking;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ca.antonious.materialdaypicker.MaterialDayPicker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FoodFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MaterialDayPicker materialDayPicker;
    TextView textViewDate, textViewName, textViewWater, textViewCalo
            , textViewDayKN, tvWaterML, tvRecordWater, tvFoodKalories
            , tvRecordFood;
    List<MaterialDayPicker.Weekday> allWeekdays;
    MaterialDayPicker.Weekday currentday;

    ImageView imageFood, imgWater, imgPlus, imgMinus ,imgRecordWater, imgRecordFood
            , imgPeriod;
    List<String> listNameFood,listUnitFood;
    List<Double> listCaloriesFood;
    ConstraintLayout cltFood, cltWater;
    AutoCompleteTextView tvFoodName;
    EditText tvUnit;

    double calories;
    int drinkingWater;
    int currentDrinkWater;

    public FoodFragment() {
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
        View view = inflater.inflate(R.layout.fragment_food, container, false);

        materialDayPicker = (MaterialDayPicker) view.findViewById(R.id.day_picker);
        textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        textViewName = (TextView) view.findViewById(R.id.textViewName);
        textViewWater = (TextView) view.findViewById(R.id.textViewWater);
        textViewDayKN = (TextView) view.findViewById(R.id.textViewDayKN);
        textViewCalo = (TextView) view.findViewById(R.id.textViewCalo);
        imageFood = (ImageView) view.findViewById(R.id.imageFood);
        imgPeriod = (ImageView) view.findViewById(R.id.imgPeriod);
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
        tvFoodKalories = (TextView) view.findViewById(R.id.tvFoodKalories);
        tvRecordFood = (TextView) view.findViewById(R.id.tvRecordFood);
        imgRecordFood = (ImageView) view.findViewById(R.id.imgRecordFood);

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
        imgPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),InstructionCounterPeriodActivity.class );
                startActivity(intent);
            }
        });
    }

    //// load du lieu  fragment khi moi vao
    public void Loaddata() {
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        textViewDate.setText(date.toString());
        //// set profile name
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("profile").child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String x = snapshot.getValue(String.class);
                textViewName.setText(x);
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
                        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                        //  OnedayofPractice onedayofPractice = snapshot.getValue(OnedayofPractice.class);
                                        if (snapshot.getValue() != null)
                                        {
                                           if (date.toString().equals(currentdate.toString()))
                                               currentDrinkWater = snapshot.child("nutrition").child("Water").getValue(Integer.class);
                                           textViewWater.setText(""+snapshot.child("nutrition").child("Water").getValue(Integer.class));
                                           int x = (int) Math.round(snapshot.child("nutrition").child("Calories").getValue(double.class));
                                           textViewCalo.setText(""+x);



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
                tvFoodName.setAdapter(adapterFood);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        ArrayAdapter<String> adapterFood = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,listNameFood);
        tvFoodName.setAdapter(adapterFood);
        tvFoodName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                calories = listCaloriesFood.get(position);
                tvUnit.setHint(listUnitFood.get(position));
                tvFoodKalories.setHint(calories + " Calories");
            }
        });
    }
    private void recordDataWater(){
        currentDrinkWater += drinkingWater;
        long millis = System.currentTimeMillis() ;
        java.sql.Date date = new java.sql.Date(millis);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                .child("nutrition").child("Water").setValue(currentDrinkWater);
        setDayPicker();
    }
    private void recordDataFood(){

    }
}