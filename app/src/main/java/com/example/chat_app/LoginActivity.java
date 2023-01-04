package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    EditText email, password;
    Button signIn, signUp;
    TextView forgotPassword;
    private FirebaseAuth auth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.editText_email_main);
        password = findViewById(R.id.editText_password_main);
        signIn = findViewById(R.id.button_signin_main);
        signUp = findViewById(R.id.button_signup_main);
        forgotPassword = findViewById(R.id.textView_forgotpwd_main);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn.setClickable(false);

                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                if (!userEmail.equals("") && !userPassword.equals("")) {
                    signIn(userEmail, userPassword);
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter an email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp.setClickable(false);
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword.setClickable(false);
                Intent i = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(i);
            }
        });
    }

    private void signIn(String userEmail, String userPassword) {
        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCustomToken:success");
                            Toast.makeText(LoginActivity.this, "Login Successful.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
//
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}




