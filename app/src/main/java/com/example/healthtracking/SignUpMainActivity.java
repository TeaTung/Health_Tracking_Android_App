package com.example.healthtracking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthtracking.ClassData.JavaMailAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Random;

public class SignUpMainActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    Button buttonSignUp;
    FirebaseAuth mAuth;
    TextView textViewBack;
    ProgressDialog progressDialog;
    View decorateView;
    boolean result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_main);
        Anhxa();
        decorView();
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
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if (!editTextEmail.getText().toString().equals("") && isValidEmail(editTextEmail.getText().toString()))
                     checkMailwasExist();
            }

            @Override
            public void afterTextChanged(Editable s) {

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
            editTextEmail.setError("Email không hợp lệ");
            return;
        }
        else if (result == true)
        {
            editTextEmail.setError("Email đã được đăng ký");
            return;
        }
        Random random = new Random();
        String code = String.valueOf(random.nextInt(900000)+100000);
        String mEmail = editTextEmail.getText().toString();
        String mSubject = "Xác nhận đăng ký tài khoản";
        String mMessage = "Xin chào, mã xác nhận tài khoản của bạn là: " + code ;

        JavaMailAPI javaMailAPI = new JavaMailAPI(SignUpMainActivity.this, mEmail, mSubject, mMessage);
        javaMailAPI.execute();

        Intent intent = new Intent(SignUpMainActivity.this, ConfirmCodeActivity.class);
        intent.putExtra("email",mEmail);
        intent.putExtra("password",password);
        intent.putExtra("code",code);
        startActivity(intent);



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

    public void checkMailwasExist()
    {
            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(editTextEmail.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            result = !task.getResult().getSignInMethods().isEmpty();
                        }


                    });


    }
}