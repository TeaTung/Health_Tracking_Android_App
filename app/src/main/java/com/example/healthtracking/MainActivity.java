package com.example.healthtracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import bolts.Task;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeUserSetting();

        chipNavigationBar = findViewById(R.id.bottom_nav_bar);
        chipNavigationBar.setItemSelected(R.id.nav_home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.nav_food:
                        fragment = new FoodFragment();
                        break;
                    case R.id.nav_run:
                        fragment = new ExcerciseFragment();
                        break;
                    case R.id.nav_account:
                        fragment = new AccountFragment();
                        break;
                }

                if(fragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
                }
            }
        });

    }

    public  void changeUserSetting(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("WAS_LOGIN",true);
        editor.putBoolean("WAS_INFORMATION",false);
        editor.apply();
    }



}