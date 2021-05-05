package com.example.healthtracking;

import androidx.appcompat.app.AppCompatActivity;
import  com.example.healthtracking.ClassData.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InformationActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText edittextname,edittextage,edittextheight, edittextweight;
    Spinner spinnersex;
    Button button;
    String arr[]={"Nam", "Nữ"};
    String Sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Anhxa();
        setupSpinner();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase = FirebaseDatabase.getInstance().getReference();
                User user = new User();
                Profile profile = new Profile(edittextname.getText().toString(), Integer.parseInt(edittextage.getText().toString()),Sex,
                        Double.parseDouble(edittextheight.getText().toString()),Double.parseDouble(edittextweight.getText().toString()));
                OnedayofPractice onedayofPractice1 = new OnedayofPractice(new Run(0,0),0,0);
                user.profile = profile;

                long millis=System.currentTimeMillis();
                java.sql.Date date=new java.sql.Date(millis);
                user.practice.put(date.toString(),onedayofPractice1);
                // textView.setText(getIntent().getStringExtra("UID").toString());
                mDatabase.child(getIntent().getStringExtra("UID").toString()).setValue(user);
                Intent intent = new Intent(InformationActivity.this, MenuActivity.class);
                intent.putExtra("UID", getIntent().getStringExtra("UID"));
                startActivity(intent);

            }
        });

    }


    public  void Anhxa()
    {
        edittextname = (EditText) findViewById(R.id.editTextName);
        edittextage = (EditText) findViewById(R.id.editTextAge);
        spinnersex = (Spinner) findViewById(R.id.spinnerSex);
        edittextheight = (EditText) findViewById(R.id.editTextHeight);
        edittextweight = (EditText) findViewById(R.id.editTextWeight);
        button = (Button) findViewById(R.id.buttonStart);
    }
    public  void setupSpinner()
    {
        //Gán Data source (arr) vào Adapter
        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        arr
                );
        //phải gọi lệnh này để hiển thị danh sách cho Spinner
        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner
        spinnersex.setAdapter(adapter);
        //thiết lập sự kiện chọn phần tử cho Spinner
        spinnersex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              Sex = arr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}