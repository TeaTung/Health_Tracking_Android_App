package com.example.healthtracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthtracking.ClassData.JavaMailAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

public class ConfirmCodeActivity extends AppCompatActivity {

    EditText editTextCode;
    TextView textViewReset,textViewNotice,textViewTimer;
    Button buttonContinue,buttonBack;
    String code,mEmail;
    CountDownTimer timer;
    FirebaseAuth mAuth;
    int dem = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code);
        mAuth = FirebaseAuth.getInstance();
        Anhxa();;
        code = getIntent().getStringExtra("code");
        mEmail = getIntent().getStringExtra("email");

        buttonContinue.setOnClickListener(this::onClick);
        textViewReset.setOnClickListener(this::onClick);
        buttonBack.setOnClickListener(this::onClick);
        editTextCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editTextCode.getText().toString().length() > 6)
                {
                    String x = editTextCode.getText().toString().substring(0,6);
                    editTextCode.setText(x);
                    editTextCode.setSelection(6);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                textViewNotice.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void onClick(View v)
    {
        if (v.getId() == R.id.buttonCoutinue)
        {
            if (editTextCode.getText().toString().equals(""))
            {
                textViewNotice.setText("Vui lòng nhập ma xác nhận");
                return;
            }
            Continue();
        }
        else  if (v.getId() == R.id.buttonBack)
        {
            Intent intent = new Intent(ConfirmCodeActivity.this, SignUpMainActivity.class);
            startActivity(intent);
            finish();
        }
        else if (v.getId() == R.id.textViewReset)
        {
            buttonContinue.setEnabled(true);
            timer.cancel();
            StartTime();
            Random random = new Random();
            code = String.valueOf(random.nextInt(900000)+100000);
            String mSubject = "Khôi phục tài khoản";
            String mMessage = "Xin chào, mã khôi phục tài khoản của bạn là: " + code ;

            JavaMailAPI javaMailAPI = new JavaMailAPI(ConfirmCodeActivity.this, mEmail, mSubject, mMessage);
            javaMailAPI.execute();
            Toast.makeText(ConfirmCodeActivity.this,"Vui lòng kiểm tra email của bạn",Toast.LENGTH_LONG).show();
        }

    }

    public  void Anhxa()
    {
        editTextCode = (EditText) findViewById(R.id.editTextCode);
        textViewReset = (TextView) findViewById(R.id.textViewReset);
        textViewNotice = (TextView) findViewById(R.id.textViewNotice) ;
        textViewTimer = (TextView) findViewById(R.id.textViewTimer) ;
        buttonContinue = (Button) findViewById(R.id.buttonCoutinue);
        buttonBack = (Button) findViewById(R.id.buttonBack);
    }

    public  void StartTime()
    {
        timer = new CountDownTimer(150000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText("Thoi gian khả dụng của mã còn: "+String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                textViewTimer.setText("Thoi gian đã hết, vui lòng gửi lại mã!");
                buttonContinue.setEnabled(false);

            }
        }.start();
    }
    @Override
    protected void onStart() {
        super.onStart();
        StartTime();
    }

    public  void Continue()
    {
        if (editTextCode.getText().toString().equals(code))
        {
            ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Vui lòng đợi...");
            progress.show();
            progress.setCanceledOnTouchOutside(false);
            String email = getIntent().getStringExtra("email");
            String password = getIntent().getStringExtra("password");

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progress.dismiss();
                        Toast.makeText(ConfirmCodeActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_LONG).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    }
                    else {
                        progress.dismiss();
                        Toast.makeText(ConfirmCodeActivity.this, "Tạo tài khoản không thành công", Toast.LENGTH_LONG).show();
                        updateUI(null);
                    }
                }
            });
        }
        else {
            dem = dem+1;
            if (dem == 3)
            {
                textViewNotice.setText("Bạn đã nhập sai 3 lần, hệ thống sẽ gửi mã khác");
                timer.cancel();
                onStart();
                textViewNotice.setVisibility(View.VISIBLE);
                Random random = new Random();
                String code = String.valueOf(random.nextInt(900000)+100000);
                String mSubject = "Khôi phục tài khoản";
                String mMessage = "Xin chào, mã khôi phục tài khoản của bạn là: " + code ;

                JavaMailAPI javaMailAPI = new JavaMailAPI(ConfirmCodeActivity.this, mEmail, mSubject, mMessage);
                javaMailAPI.execute();
            }
        }

    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(ConfirmCodeActivity.this, InformationActivity.class);
            intent.putExtra("UID", user.getUid());
            setSharedPreference(user.getUid());
            startActivity(intent);
            finish();
        }

    }
    public void setSharedPreference(String UID) {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UID", UID);
        editor.apply();
        String x = sharedPreferences.getString("UID","");

        Toast.makeText(ConfirmCodeActivity.this, x, Toast.LENGTH_LONG).show();
    }
}