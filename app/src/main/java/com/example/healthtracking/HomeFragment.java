package com.example.healthtracking;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SensorEventListener {
    TextView stepCounter;
    SensorManager sensorManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView textViewName, textViewKm, textViewCalo, textViewCountStep;
    ProgressBar progressBarStepCount;

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

        textViewKm = (TextView) view.findViewById(R.id.textViewKm);
        textViewCalo = (TextView) view.findViewById(R.id.textViewCalo);
        textViewCountStep = (TextView) view.findViewById(R.id.stepsCounter);
        progressBarStepCount = (ProgressBar) view.findViewById(R.id.stepProgress);
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
        return view;

    }


    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stepCounter = (TextView) getActivity().findViewById(R.id.stepsCounter);
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
       String stepcount ;
       stepcount = String.valueOf(event.values[0]);
       int x = (int) Double.parseDouble(stepcount);
       stepCounter.setText(String.valueOf(x));
       Setup();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void Setup ()
    {
        double m, calo;
        int stepcount = (int) Double.parseDouble(stepCounter.getText().toString());
        progressBarStepCount.setProgress(stepcount);
        m = Math.round(stepcount * 0.7*100)/100;
        calo = m * 0.0625;
        if (m <1000) textViewKm.setText(String.valueOf(m)+" m");
        else
        {
            m = m/1000;
            textViewKm.setText(String.valueOf(m)+" Km");
        }
        textViewCalo.setText(String.valueOf(calo)+" Calo");
    }
}