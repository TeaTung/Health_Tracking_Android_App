package com.example.healthtracking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;


public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    CallbackManager mCallbackManager;
    LoginButton buttonLoginFB;
    SignInButton buttonSignInGG;
    EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    TextView textViewSignUp, textViewForgetPW;
    Button  buttonSignIn;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Anhxa();
        mAuth = FirebaseAuth.getInstance();
        Facebook();
        Google();
        buttonSignIn.setOnClickListener(this::onClick);
        buttonLoginFB.setOnClickListener(this::onClick);
        buttonSignInGG.setOnClickListener(this::onClick);
        textViewSignUp.setOnClickListener(this::onClick);
        textViewForgetPW.setOnClickListener(this::onClick);

    }

    public void onClick(View v) {
        if (v.getId() == R.id.buttonSignIn)
        {
            LoginEmail();
        }
        else  if (v.getId() == R.id.signinGGbutton)
        {
            signIn();
        }
        else  if (v.getId() ==R.id.textViewSignup)
        {
            Intent intent = new Intent(LoginActivity.this, SignUpMainActivity.class);
            startActivity(intent);
            finish();
        }
        else  if (v.getId() == R.id.textViewForgetPassword)
        {
            String email = editTextEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Nhập email của ban!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Kiểm tra email để đặt lại mật khẩu!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Qúa trình gửi mail thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

   /* @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser User = mAuth.getCurrentUser();
        updateUI(User);
    }*/



    public  void Anhxa()
    {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignup);
        textViewForgetPW = (TextView) findViewById(R.id.textViewForgetPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonLoginFB = (LoginButton) findViewById(R.id.loginFB_button);
        buttonSignInGG = (SignInButton) findViewById(R.id.signinGGbutton);
    }
    private void Facebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mCallbackManager = CallbackManager.Factory.create();
        buttonLoginFB.setReadPermissions("email", "public_profile");
        buttonLoginFB.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private  void Google()
    {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private  void LoginEmail()
    {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

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

      /*  mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_LONG).show();
                }
            }
        });*/
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Đăng nhập không thành công", Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }
                    }
                });

    }

    public  boolean isValidEmail(CharSequence target)
    {
        return (TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(LoginActivity.this, InformationActivity.class);
            intent.putExtra("UID", user.getUid());
            startActivity(intent);
            finish();
        } else
            Toast.makeText(this, "Đăng nhập thất bại! ", Toast.LENGTH_SHORT).show();

    }
}