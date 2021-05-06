package com.example.healthtracking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpMainActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    Button buttonSignIn;
    FirebaseAuth mAuth;
    TextView buttonSignUp;
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

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
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
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
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
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Vui lòng đợi...");
        progress.show();
        progress.setCanceledOnTouchOutside(false);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progress.dismiss();
                    Toast.makeText(SignUpMainActivity.this, "Tạo tài khoản thành công", Toast.LENGTH_LONG).show();
                }
                else {
                    progress.dismiss();
                    Toast.makeText(SignUpMainActivity.this, "Tạo tài khoản không thành công", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public  boolean isValidEmail(CharSequence target)
    {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}