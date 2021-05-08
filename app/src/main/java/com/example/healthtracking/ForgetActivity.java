package com.example.healthtracking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgetActivity extends AppCompatActivity {

    EditText editTextMail;
    TextView textViewNotice;
    Button  buttonSendCode, buttonBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        editTextMail = (EditText) findViewById(R.id.editTextEmail);
        buttonSendCode = (Button) findViewById(R.id.buttonSendCode);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        textViewNotice = (TextView) findViewById(R.id.textViewNotice);
        textViewNotice.setVisibility(View.INVISIBLE);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgetActivity.this, LoginActivity.class);
                //intent.putExtra("email",mEmail);
                //intent.putExtra("code",code);
                startActivity(intent);
                finish();
            }
        });
        buttonSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextMail.getText().toString().equals(""))
                {
                    textViewNotice.setText("Vui lòng nhập email");
                    textViewNotice.setVisibility(View.VISIBLE);
                    return;
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(editTextMail.getText().toString()).matches())
                {
                    textViewNotice.setText("Email không tồn tại");
                    textViewNotice.setVisibility(View.VISIBLE);
                    return;
                }
                else if (!checkAccountEmailExistInFirebase(editTextMail.getText().toString()))
                {
                    senEmail();
                }
                else if (checkAccountEmailExistInFirebase(editTextMail.getText().toString()))
                {
                    textViewNotice.setText("Email này chưa tạo tài khoản");
                    textViewNotice.setVisibility(View.VISIBLE);
                    return;
                }

            }
        });

    }

    private void senEmail() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(editTextMail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            textViewNotice.setText("Gửi email thanh công, vui lòng kiểm tra email");
                            textViewNotice.setVisibility(View.VISIBLE);

                        } else {
                            textViewNotice.setText("Gửi email thất bại");
                            textViewNotice.setVisibility(View.VISIBLE);
                        }
                    }
                });


    }

    private boolean checkAccountEmailExistInFirebase(String email) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final boolean[] b = new boolean[1];

        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                b[0] = !task.getResult().getSignInMethods().isEmpty();
            }
        });
        return b[0];
    }
    }
