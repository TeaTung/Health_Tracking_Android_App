package com.example.healthtracking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthtracking.ClassData.Run;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SensorEventListener {
    SensorManager sensorManager;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    HomeAdapter adapter;
    int[] stepsCounter;
    ProgressBar progressBar;
    int realStepCounter = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView textViewName;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment home.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        textViewName = (TextView) view.findViewById(R.id.textView14);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycle_view);
        progressBar = (ProgressBar)view.findViewById(R.id.stepProgress);

        setProfileName();
        sendCardViewIntoRecycleView(recyclerView);

        sharedPreferences = getActivity().getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
        realStepCounter = sharedPreferences.getInt("REALSTEP",0);
        return view;

    }
    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        setTodayStepCounter();
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void setTodayStepCounter(){
        Calendar today = Calendar.getInstance();
        int currentDay = today.get(Calendar.DAY_OF_MONTH);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
        if (currentDay != sharedPreferences.getInt("DAY",0)){

            realStepCounter = 0;
            realStepCounter++;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("REALSTEP",realStepCounter);
            editor.putInt("DAY",currentDay);
            editor.apply();

        } else {
            realStepCounter++;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("REALSTEP",realStepCounter);
            editor.apply();
        }
    }
    public void sendCardViewIntoRecycleView(RecyclerView recyclerView){
        Home[] cardSteps = {
                new Home(realStepCounter,progressBar)
        };

        stepsCounter = new int[cardSteps.length];
        for (int i = 0; i < stepsCounter.length; i++){
            stepsCounter[i] = cardSteps[i].getStepsCounter();
        }

        adapter = new HomeAdapter(stepsCounter);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }
    public void setProfileName(){
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

//    public void requestStepCounterPermission(){
//        if(ContextCompat.checkSelfPermission(getContext(),
//                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
//            //ask for permission
//            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PHY);
//        }
//    }
}