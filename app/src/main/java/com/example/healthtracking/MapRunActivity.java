package com.example.healthtracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
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
import java.util.Timer;
import java.util.TimerTask;

public class MapRunActivity extends AppCompatActivity implements OnMapReadyCallback{
    SupportMapFragment supportMapFragment;
    View decorateView;
    PolylineOptions line;
    ImageView imgStart;
    Button btnStop;
    ConstraintLayout lytNotification;
    FusedLocationProviderClient fusedLocationClient;
    SensorManager sensorManager;
    TextView tvStep, tvTimeRecord, tvDistance, tvSpeed, tvCalo, tvFireFit;
    ProgressBar pgbAward;
    LocationRequest locationRequest;
    GoogleMap mGoogleMap;
    Timer timer;
    TimerTask timerTask;
    LatLng previousLatLngCallBack;

    double step;
    double distance;
    double calo;
    double speed;
    double time = 0.0;
    boolean isTimerStarted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_run);

        //Initialize google map
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
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        tvStep = (TextView) findViewById(R.id.tvStep);
        tvCalo = (TextView) findViewById(R.id.tvCalo);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvSpeed = (TextView) findViewById(R.id.tvSpeed);
        tvTimeRecord = (TextView) findViewById(R.id.tvTimeRecord);
        tvFireFit = (TextView) findViewById(R.id.fire_fit);
        pgbAward = (ProgressBar) findViewById(R.id.pgbAward);
        pgbAward.setMax(1);
        timer = new Timer();

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
                if (location != null) {
                    //sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
                            mGoogleMap = googleMap;

                            //Create marker
                            LatLng startLocation = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            MarkerOptions startMaker = new MarkerOptions().position(startLocation)
                                    .title("Bạn đang ở đây");

                            //Zoom map and add marker
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17));
                            mGoogleMap.addMarker(startMaker);

                            //Configure line
                            line = new PolylineOptions().width(15).color(Color.BLUE);
                            line.add(startLocation);
                        }
                    });
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                Location location = locationList.get(locationList.size() - 1);
                LatLng lastLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (previousLatLngCallBack != null){
                    setDistanceChange(previousLatLngCallBack,lastLatLng);
                }

                line.add(lastLatLng);
                mGoogleMap.addPolyline(line);
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 17));
                previousLatLngCallBack = lastLatLng;

            }
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorateView.setSystemUiVisibility(hideSystemBar());
        }
    }
    private int hideSystemBar() {
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }
    public void callImageStart() {
        imgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStop.setText("STOP");
                imgStart.setVisibility(View.INVISIBLE);
                lytNotification.setVisibility(View.VISIBLE);
                step = 0;
                isTimerStarted = true;

                startTrackingStep();
                startTimer();
            }
        });
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        tvTimeRecord.setText(getTimerText());
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask,0,1000);
    }

    private String getTimerText() {
        int rounded = (int) Math.round(time);

        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) /60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }

    public void callButtonStop() {
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnStop.getText().equals("STOP")) {
                    btnStop.setText("OK");
                    isTimerStarted = false;
                    timerTask.cancel();
                    time = 0.0;


                    if (ActivityCompat.checkSelfPermission(MapRunActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapRunActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mGoogleMap.setMyLocationEnabled(false);
                } else if (btnStop.getText().equals("OK")){
                    lytNotification.setVisibility(View.INVISIBLE);
                    imgStart.setVisibility(View.VISIBLE);
                    tvTimeRecord.setText(formatTime(0,0,0));
                }
            }
        });
    }
    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {

    }

    public void startTrackingStep(){
        //Request location update
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(MapRunActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapRunActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        mGoogleMap.setMyLocationEnabled(true);
    }

    private void setDistanceChange(LatLng previousLocation, LatLng currentLocation){
        double theTa = previousLocation.latitude - currentLocation.latitude;
        double dist = Math.sin(deg2rad(previousLocation.latitude)) * Math.sin(deg2rad(currentLocation.latitude))
                + Math.cos(deg2rad(previousLocation.latitude)) * Math.cos(deg2rad(currentLocation.latitude))
                * Math.cos(deg2rad(theTa));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        setTextInNotificationBackGround(dist);
    }

    private double deg2rad(double latitude) {
        return (latitude * Math.PI /180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void setTextInNotificationBackGround(double dist){
        distance += Math.round(dist*10) /10;
        calo = Math.round(distance * 0.0625 * 10) / 10;
        speed = Math.round((distance / time) * 1000 * 10) / 10;
        step = Math.round(distance * 0.7 * 10) / 10;

        tvStep.setText(step + " bước chân ");
        tvDistance.setText(distance + " kms ");
        tvCalo.setText(calo + " calories ");
        tvSpeed.setText(speed + " m/s ");
        pgbAward.setProgress((int)step);
        if (pgbAward.getProgress() == 100){
            tvFireFit.setVisibility(View.VISIBLE);
        }
    }
}