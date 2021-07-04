package com.example.healthtracking;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthtracking.CardView.Exercises;
import com.example.healthtracking.CardView.Food;
import com.example.healthtracking.CardView.FunFact;
import com.example.healthtracking.CardView.Item;
import com.example.healthtracking.CardView.PersonalInformation;
import com.example.healthtracking.ClassData.Exercise;
import com.example.healthtracking.ClassData.Jog;
import com.example.healthtracking.ClassData.Nutrition;
import com.example.healthtracking.ClassData.OnedayofPractice;
import com.example.healthtracking.ClassData.Profile;
import com.example.healthtracking.ClassData.Run;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SensorEventListener {
    SensorManager sensorManager;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    ProgressBar progressBarStep;
    ProgressBar progressBarFood;
    TextView textViewName;
    List<Item> items = new ArrayList<>();
    PersonalInformation personalInformation;
    Exercises exercises;
    Food food;
    FunFact funFact;

    int realStepCounter , height,weigth, daykn;
    long nextDate;
    double kalo, distance;
    int sex;
    int check = 0;
    int water;
    double foodCalories;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        textViewName = (TextView) view.findViewById(R.id.textView14);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        setProfileName();
        LoaddataForFirst();
        sharedPreferences = getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        realStepCounter = sharedPreferences.getInt("REALSTEP",0)-1;
        requestStepCounterPermission();
        createCardView();
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
        sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (check == 1) {
            realStepCounter++;
            distance = Math.round(realStepCounter * 0.7 * 100) / 100;
            kalo = Math.round(distance * 0.0625);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("REALSTEP", realStepCounter);
            editor.apply();
            items.clear();
            createCardView();
            UpdateData();
        }
        else
        {
            LoaddataForFirst();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setProfileName() {
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

    public void requestStepCounterPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 44);
        }
    }

    public void createCardView(){
        setInformationCardView();

        //Type: 1 = femaleInfo || 0 = MaleInfo || 2 = Exercise || 3 = Food || 4 = Fun fact


        items.add(new Item(sex,personalInformation));
        items.add(new Item(2,exercises));
        items.add(new Item(3,food));
        items.add(new Item(4,funFact));

        recyclerView.setAdapter(new HomeAdapter(items));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setInformationCardView(){

        personalInformation = new PersonalInformation(height,weigth,daykn);
        exercises = new Exercises(realStepCounter,progressBarStep,kalo,distance);
        food = new Food(foodCalories,water,progressBarFood);
        funFact = new FunFact("title","quote");
    }



    public  void LoaddataForFirst()
    {
        long millis = System.currentTimeMillis() ;
        java.sql.Date date = new java.sql.Date(millis);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                Profile x = snapshot.child("profile").getValue(Profile.class);
                height = x.Height;
                weigth = x.Weight;
                if (x.Sex.equals("Nam")) sex = 0;
                else sex = 1;
                nextDate = x.DetailPeriod.NextDate;
                daykn = setDatePeriod();
                if (snapshot.child("practice").child(date.toString()).getValue() != null) {
                    water = snapshot.child("practice").child(date.toString()).child("nutrition").child("Water").getValue(Integer.class);
                    foodCalories = snapshot.child("practice").child(date.toString()).child("nutrition").child("Calories").getValue(double.class);
                }
                else {
                    OnedayofPractice onedayofPractice = new OnedayofPractice(new Run(), new Nutrition(),0,new Jog(), new Exercise());
                    FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                            .setValue(onedayofPractice);
                    water = 0;
                    foodCalories = 0;
                }
                realStepCounter++;
                distance = Math.round(realStepCounter * 0.7 * 100) / 100;
                kalo = Math.round(distance * 0.0625);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("REALSTEP", realStepCounter);
                editor.apply();
                items.clear();
                createCardView();
                check=1;



            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    //// cap nhat du lieu len firebase
    public void UpdateData()
    {
        Run run = new Run(realStepCounter, distance, kalo);
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null)
                        {
                            FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                                    .child("run").setValue(run);
                        }
                        else
                        {
                            OnedayofPractice onedayofPractice = new OnedayofPractice(run, new Nutrition(),0,new Jog(), new Exercise());
                            FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("practice").child(date.toString())
                                    .setValue(onedayofPractice);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
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

}