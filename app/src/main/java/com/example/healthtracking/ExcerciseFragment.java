package com.example.healthtracking;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.healthtracking.ClassData.Run;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
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
 * Use the {@link ExcerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExcerciseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MaterialDayPicker materialDayPicker;
    TextView textViewDate, textViewName, textViewStepCount, textViewKalos, textViewTime, textViewHistory, textViewTimeUnit;
    List<MaterialDayPicker.Weekday> allWeekdays;
    MaterialDayPicker.Weekday currentday;
    Spinner lvListExercise;
    ImageView imgStart;
    TextView tvStart;


    public ExcerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExcerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExcerciseFragment newInstance(String param1, String param2) {
        ExcerciseFragment fragment = new ExcerciseFragment();
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
        View view = inflater.inflate(R.layout.fragment_excercise, container, false);
        materialDayPicker = (MaterialDayPicker) view.findViewById(R.id.day_picker);
        textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        textViewName = (TextView) view.findViewById(R.id.textViewName);
        textViewStepCount = (TextView) view.findViewById(R.id.textViewCalo);
        textViewKalos = (TextView) view.findViewById(R.id.textViewKalosUse);
        textViewTime = (TextView) view.findViewById(R.id.textViewWater);
        textViewTimeUnit = (TextView) view.findViewById(R.id.textViewTimeUnit);
        textViewHistory = (TextView) view.findViewById(R.id.textViewHistory);
        Loaddata();
        setDayPicker();
     
        textViewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                startActivity(intent);
            }
        });
      
        lvListExercise = (Spinner) view.findViewById(R.id.lvListExercise);
        imgStart = (ImageView) view.findViewById(R.id.imgStart);
        tvStart = (TextView) view.findViewById(R.id.tvStart);
        setDayPicker();
        Loaddata();
        setButtonStart();
        setListExercise();
        // Inflate the layout for this fragment

        return view;
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

    /// cai dat de daypicker hien thi theo ngay hien
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
                        java.sql.Date date = new java.sql.Date(millis);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                        //  OnedayofPractice onedayofPractice = snapshot.getValue(OnedayofPractice.class);
                                        if (snapshot.getValue() != null)
                                        {

                                            Run run = snapshot.child("run").getValue(Run.class);
                                            int jogTime =  snapshot.child("jog").child("Time").getValue(Integer.class);
                                            double jogCalo =  snapshot.child("jog").child("Calories").getValue(double.class);
                                            int exTime =  snapshot.child("exercise").child("Time").getValue(Integer.class);
                                            double exCalo =  snapshot.child("exercise").child("Calories").getValue(double.class);
                                            textViewStepCount.setText("" + run.StepCount);
                                            textViewKalos.setText(""+Math.round(run.Calories + jogCalo+exCalo));
                                            textViewTime.setText(ConvertTimeToString(jogTime+exTime));
                                            if (jogTime + exTime >= 3600)  textViewTimeUnit.setText("Giờ");
                                            else if (jogTime +exTime >= 60)  textViewTimeUnit.setText("Phút");
                                            else textViewTimeUnit.setText("Giây");

                                        }
                                        else

                                        {
                                            textViewStepCount.setText("0");
                                            textViewKalos.setText("0");
                                            textViewTime.setText("0");
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


    public  String ConvertTimeToString(int i) {
        if (i >= 3600) {
            double x = (i * 1.0) / 3600;
            if (Math.round(x) == i / 3600) return "" + i / 3600;
            return "" + Math.round(x * 100.0) / 100.0;
        } else if (i >= 60) {
            double x = (i * 1.0) / 60;
            if (Math.round(x) == i / 60) return "" + i / 60;
            return "" + Math.round(x * 100.0) / 100.0;
        } else return "" + i;
    }

    private void setListExercise(){
        String arr[] = {"Hít đất", "Gập bụng", "Hít xà", "Chạy bộ", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line,arr);
        lvListExercise.setAdapter(adapter);
    }

    private void setButtonStart(){
        imgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise();
            }
        });

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExercise();
            }
        });
    }

    private void startExercise(){
        if (lvListExercise.getSelectedItem().toString().equals("") == false){
            String exercise = lvListExercise.getSelectedItem().toString();
            Intent intent = new Intent(getActivity(),RequestPermission.class);
            intent.putExtra("Name",exercise);
            startActivity(intent);
        }
    }
}