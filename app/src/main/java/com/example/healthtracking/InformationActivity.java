package com.example.healthtracking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthtracking.ClassData.Exercise;
import com.example.healthtracking.ClassData.Goal;
import com.example.healthtracking.ClassData.Jog;
import com.example.healthtracking.ClassData.Nutrition;
import com.example.healthtracking.ClassData.OnedayofPractice;
import com.example.healthtracking.ClassData.PeriodTracking;
import com.example.healthtracking.ClassData.Profile;
import com.example.healthtracking.ClassData.Run;
import com.example.healthtracking.ClassData.User;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.Calendar;

public class InformationActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    TextInputEditText edittextname,edittextheight, edittextweight;
    MaterialAutoCompleteTextView textViewyear, textViewSex;
    // Spinner spinnersex;
    Button button;
    String arr[]={"Nam", "Nữ"};
    String Sex;
    FirebaseAuth mAuth;
    View decorateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Anhxa();
        ChooseSex();
        changeUserSetting();
        textViewyear.setOnClickListener(this::onClick);
        button.setOnClickListener(this::onClick);
        decorView();
    }
    public void decorView(){
        decorateView = getWindow().getDecorView();
        decorateView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0) {
                    decorateView.setSystemUiVisibility(hideSystemBar());
                }
            }
        });
    }
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
    public  void Anhxa()
    {
        edittextname = (TextInputEditText) findViewById(R.id.editTextName);
        textViewyear = (MaterialAutoCompleteTextView) findViewById(R.id.textViewYear);
        textViewSex = (MaterialAutoCompleteTextView) findViewById(R.id.spinnerSex);
        edittextheight = (TextInputEditText) findViewById(R.id.editTextHeight);
        edittextweight = (TextInputEditText) findViewById(R.id.editTextWeight);
        button = (Button) findViewById(R.id.buttonStart);
    }


    public  void ChooseSex()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr);
        textViewSex.setAdapter(adapter);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.textViewYear) {
            final Calendar today = Calendar.getInstance();
            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(InformationActivity.this,
                    new MonthPickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(int selectedMonth, int selectedYear) {
                            textViewyear.setText(String.valueOf(selectedYear));
                        }
                    }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

            builder.setActivatedMonth(today.get(Calendar.MONTH))
                    .setMinYear(1900)
                    .setActivatedYear(today.get(Calendar.YEAR))
                    .setMaxYear(today.get(Calendar.YEAR))
                    .setTitle("Chọn năm sinh")
                    .showYearOnly()
                    .build().show();

        }
        else if (v.getId() == R.id.buttonStart)
        {
            if (checkInformation()) {
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser fuser = mAuth.getCurrentUser();
                edittextname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

                User user = new User();
                Profile profile = new Profile(edittextname.getText().toString(), Integer.parseInt(textViewyear.getText().toString()), textViewSex.getText().toString(),
                        Integer.parseInt(edittextheight.getText().toString()), Integer.parseInt(edittextweight.getText().toString()), new PeriodTracking(), new Goal(2000,2000,2000),0);
                OnedayofPractice onedayofPractice1 = new OnedayofPractice(new Run(), new Nutrition(), 0,new Jog(), new Exercise());
                user.profile = profile;

                long millis = System.currentTimeMillis();
                java.sql.Date date = new java.sql.Date(millis);
                user.practice.put(date.toString(), onedayofPractice1);
                mDatabase.child(fuser.getUid()).setValue(user);

                String sex = textViewSex.getText().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("SEX", sex);
                editor.apply();
                Intent intent = new Intent(InformationActivity.this, MainActivity.class);
                intent.putExtra("UID", getIntent().getStringExtra("UID"));
                startActivity(intent);
                finish();
            }
        }
    }

    public  void changeUserSetting(){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("WAS_LOGOUT",false);
        editor.putBoolean("WAS_INFORMATION",true);
        editor.apply();
    }

    public  boolean checkInformation()
    {
        if (edittextname.getText().toString().equals(""))
        {
            edittextname.setError("Vui long nhập tên");
            return false;
        }

        if (textViewyear.getText().toString().equals(""))
        {

            textViewyear.setError("Vui long chọn năm sinh");
            return false;
        }
        if (textViewSex.getText().toString().equals(""))
        {
            textViewSex.setError("Vui long chon gioi tinh");
            return false;
        }
        if (edittextheight.getText().toString().equals(""))
        {
            edittextheight.setError("Vui long nhập chiều cao");
            return false;
        }
        if (edittextweight.getText().toString().equals(""))
        {
            edittextweight.setError("Vui long nhập cân nặng");
            return false;
        }

        for (int i = 0; i < edittextname.getText().toString().length(); i++)
        {
            int x = (int)(edittextname.getText().toString().charAt(i));
            if (((x>= 33) & (x<=64)) | ((x >= 91) & (x <= 96)) | ((x >= 123)& (x <= 126)))
            {
                edittextname.setError("Tên chỉ bao gôm chữ cái và ký tự trắng");
                return false;
            }

        }
        try {
           Double x = Double.parseDouble(edittextheight.getText().toString());
           if (( x <= 14) | (x>= 301))
           {
             edittextheight.setError("Chiều cao từ 15cm tới 300cm");
               return  false;
           }
        }
        catch (Exception e )
        {
            /////////
            return false;
        }

        try {
            Double x = Double.parseDouble(edittextweight.getText().toString());
            if (( x <= 9) | (x>= 301))
            {
                edittextweight.setError("Cân nặng phải từ 10kg tới 300kg ");
                return  false;
            }
        }
        catch (Exception e )
        {
            /////////
            return false;
        }
        return true;
    }

}
