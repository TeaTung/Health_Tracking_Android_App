package com.example.healthtracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    ImageView imgGoal, imgInformation, imgChart,imgLogout;
    TextView tvGoalHeader, tvGoalSubHeader, tvInformationHeader, tvInformationSubHeader, tvChartHeader,
            tvCharSubHeader, tvLogout, textViewName;
    ConstraintLayout goal, chart, information;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        imgChart = view.findViewById(R.id.imgChart);
        imgGoal = view.findViewById(R.id.imgGoal);
        imgInformation = view.findViewById(R.id.imgInformation);
        imgLogout = view.findViewById(R.id.imgLogout);
        tvCharSubHeader = view.findViewById(R.id.tvCharSubHeader);
        tvChartHeader = view.findViewById(R.id.tvChartHeader);
        tvGoalHeader = view.findViewById(R.id.tvGoalHeader);
        textViewName = view.findViewById(R.id.textViewAccoutName);
        tvGoalSubHeader = view.findViewById(R.id.tvGoalSubHeader);
        tvInformationHeader = view.findViewById(R.id.tvInformationHeader);
        tvInformationSubHeader = view.findViewById(R.id.tvInformationSubHeader);
        tvLogout = view.findViewById(R.id.tvLogout);
        chart = view.findViewById(R.id.chart);
        goal = view.findViewById(R.id.goal);
        information = view.findViewById(R.id.information);
        LoadName();
        setOnClick();
        // Inflate the layout for this fragment
        return view;
    }

    public  void LoadName()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String x = snapshot.child("Name").getValue(String.class);
                textViewName.setText(x);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    private void setOnClick(){
        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChart();
            }
        });
        imgChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChart();
            }
        });
        tvChartHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChart();
            }
        });
        tvCharSubHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChart();
            }
        });
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInformation();
            }
        });
        imgInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInformation();
            }
        });
        tvInformationSubHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInformation();
            }
        });
        tvInformationHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInformation();;
            }
        });
        goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGoal();
            }
        });
        imgGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGoal();
            }
        });
        tvGoalSubHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGoal();
            }
        });
        tvGoalHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGoal();
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPrefs",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("WAS_LOGIN",false);
                editor.putBoolean("WAS_INFORMATION",false);
                editor.putBoolean("WAS_LOGOUT", true);
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
    private void startChart(){
        Intent intent = new Intent(getActivity(),GraphAccountActivity.class);
        startActivity(intent);
    }
    private void startGoal(){
        Intent intent = new Intent(getActivity(), GoalAccountActivity.class);
        startActivity(intent);
    }
    private void startInformation(){
        Intent intent = new Intent(getActivity(),InformationAccountActivity.class);
        startActivity(intent);
    }
}