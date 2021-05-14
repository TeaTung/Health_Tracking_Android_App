package com.example.healthtracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthtracking.ClassData.JavaMailAPI;
import com.example.healthtracking.ClassData.UserSetting;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class SignUpMainActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    Button buttonSignUp;
    FirebaseAuth mAuth;
    TextView textViewBack;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_main);
        Anhxa();
        mAuth = FirebaseAuth.getInstance();
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(SignUpMainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public  void Anhxa()
    {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        textViewBack = (TextView) findViewById(R.id.textViewBack);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
    }

    public  void Register()
    {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmpw = editTextConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            editTextEmail.setError("Nhập email của bạn");
            return;
        }
        else if (TextUtils.isEmpty(password))
        {
            editTextPassword.setError("Nhập mật khẩu của bạn");
            return;
        }
        else if (TextUtils.isEmpty(confirmpw))
        {
            editTextConfirmPassword.setError("Xác nhận mật khẩu của bạn");
            return;
        }
        else if (!password.equals(confirmpw))
        {
            editTextConfirmPassword.setError("Mật khẩu không giống ");
            return;
        }
        if (password.length() <=4)
        {
            editTextPassword.setError("Mật khẩu phải lớn hơn 4 ký tự");
            return;
        }
        else if (!isValidEmail(email))
        {
            editTextEmail.setError("Email không tồn tại");
            return;
        }
        Random random = new Random();
        String code = String.valueOf(random.nextInt(900000)+100000);
        String mEmail = editTextEmail.getText().toString();
        String mSubject = "Khôi phục tài khoản";
        String mMessage = "Xin chào, mã khôi phục tài khoản của bạn là: " + code ;

        JavaMailAPI javaMailAPI = new JavaMailAPI(SignUpMainActivity.this, mEmail, mSubject, mMessage);
        javaMailAPI.execute();

        Intent intent = new Intent(SignUpMainActivity.this, ConfirmCodeActivity.class);
        intent.putExtra("email",mEmail);
        intent.putExtra("password",password);
        intent.putExtra("code",code);
        startActivity(intent);
        finish();


    }

    public  boolean isValidEmail(CharSequence target)
    {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

  /*  public void setSharedPreference(String UID){
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UID",UID);
        editor.apply();
    }*/
}