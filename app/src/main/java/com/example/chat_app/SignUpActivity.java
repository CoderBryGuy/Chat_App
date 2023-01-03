package com.example.chat_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    CircleImageView imageViewCircle;
    EditText email, password, username;
    Button signUp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        imageViewCircle = findViewById(R.id.imageViewCircle_signUp);
        email = findViewById(R.id.editText_email_signUp);
        password = findViewById(R.id.editText_password_signUp);
        username = findViewById(R.id.editText_username_signUp);
        signUp = findViewById(R.id.button_signUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}