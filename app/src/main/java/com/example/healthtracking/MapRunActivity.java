package com.example.healthtracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MapRunActivity extends AppCompatActivity implements OnMapReadyCallback, SensorEventListener {
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    View decorateView;
    ImageView imgStart;
    Button btnStop;
    ConstraintLayout lytNotification;
    FusedLocationProviderClient fusedLocationClient;
    SensorManager sensorManager;
    TextView tvStep, tvTimeRecord, tvDistance, tvSpeed, tvCalo;
    ProgressBar pgbAward;
    int step;
    double distance;
    double calo;
    double speed;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_run);

        //Initialize google map
        client = LocationServices.getFusedLocationProviderClient(this);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);
        //Decorate view
        decorateView = getWindow().getDecorView();
        decorateView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorateView.setSystemUiVisibility(hideSystemBar());
                }
            }
        });

        //Assign implement
        imgStart = (ImageView) findViewById(R.id.imgStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        lytNotification = (ConstraintLayout) findViewById(R.id.notification_background);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        tvStep = (TextView)findViewById(R.id.tvStep);
        tvCalo = (TextView)findViewById(R.id.tvCalo);
        tvDistance = (TextView)findViewById(R.id.tvDistance);
        tvSpeed = (TextView)findViewById(R.id.tvSpeed);
        tvTimeRecord = (TextView)findViewById(R.id.tvTimeRecord);
        pgbAward = (ProgressBar)findViewById(R.id.pgbAward);
        pgbAward.setMax(100);

        //Call function
        checkAccessLocationPermission();
        getCurrentLocation();
        callButtonStop();
        callImageStart();

    }


    public void checkAccessLocationPermission() {
        if (ActivityCompat.checkSelfPermission(MapRunActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(MapRunActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    //sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
                            //Create marker
                            LatLng startLocation = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            MarkerOptions startMaker = new MarkerOptions().position(startLocation)
                                    .title("Bạn đang ở đây");

                            //Zoom map and add marker
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation,15));
                            googleMap.addMarker(startMaker);

                            PolylineOptions line = new PolylineOptions().width(15).color(Color.BLUE);
                            line.add(startLocation);
                            drawLastLocation(googleMap,line);
                        }
                    });
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == 44){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            decorateView.setSystemUiVisibility(hideSystemBar());
        }
    }
    private int hideSystemBar() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }
    public void callImageStart(){
        imgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStop.setText("STOP");
                imgStart.setVisibility(View.INVISIBLE);
                lytNotification.setVisibility(View.VISIBLE);

                step = 0;
            }
        });
    }
    public void callButtonStop(){
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnStop.getText().equals("STOP")) {
                    btnStop.setText("OK");
                } else if (btnStop.getText().equals("OK")){
                    lytNotification.setVisibility(View.INVISIBLE);
                    imgStart.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {

    }
    public void drawLastLocation(GoogleMap googleMap, PolylineOptions line) {
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LatLng latLng = new LatLng(locationResult.getLastLocation().getLatitude()
                        , locationResult.getLastLocation().getLongitude());

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                line.add(latLng);
            }
        };
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
//        step++;
//        distance = Math.round(step* 0.7*100)/100;
//        calo = distance * 0.0625;
//        speed = distance / time;
//
//        tvStep.setText("" + step);
//        tvDistance.setText("" + distance);
//        tvCalo.setText("" + calo);
//        tvSpeed.setText("" + speed);
//        pgbAward.setProgress(step);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void runTimer(){

    }
}