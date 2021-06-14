package com.example.healthtracking;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthtracking.ClassData.OnedayofPractice;
import com.example.healthtracking.ClassData.Run;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ca.antonious.materialdaypicker.MaterialDayPicker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExcerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExcerciseFragment extends Fragment {
    ImageView imgRun;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MaterialDayPicker materialDayPicker;
    TextView textViewDate, textViewName, textViewStepCount;
    List<MaterialDayPicker.Weekday> allWeekdays;
    MaterialDayPicker.Weekday currentday;
    int k;

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
        imgRun = (ImageView) view.findViewById(R.id.imgRun);
        materialDayPicker = (MaterialDayPicker) view.findViewById(R.id.day_picker);
        textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        textViewName = (TextView) view.findViewById(R.id.textViewName);
        textViewStepCount = (TextView) view.findViewById(R.id.textViewStepCount);
        Loaddata();
        setDayPicker();
        imgRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapRunActivity.class);
                startActivity(intent);
            }
        });
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
        materialDayPicker.setFirstDayOfWeek(MaterialDayPicker.Weekday.TUESDAY);
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
                                .child("run").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                //  OnedayofPractice onedayofPractice = snapshot.getValue(OnedayofPractice.class);
                                Run run = snapshot.getValue(Run.class);
                                textViewStepCount.setText("" + run.StepCount);

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
}

