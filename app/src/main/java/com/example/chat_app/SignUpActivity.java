package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    CircleImageView imageViewCircle;
    EditText email, password, userName;
    Button signUp;
    boolean imageControl = false;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        imageViewCircle = findViewById(R.id.imageViewCircle_signUp);
        email = findViewById(R.id.editText_email_signUp);
        password = findViewById(R.id.editText_password_signUp);
        userName = findViewById(R.id.editText_username_signUp);
        signUp = findViewById(R.id.button_signUp);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        imageViewCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    imageChooser();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String username = userName.getText().toString();

                if(!userEmail.equals("") && !userPassword.equals("") && username.equals("")){
                    signUp(userEmail, userPassword, username);
                }else{
                    Toast.makeText(SignUpActivity.this, "Enter email, password, and username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUp(String email, String password, String username) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                        reference.child("Users").child(auth.getUid()).child("userName").setValue(userName);
                }
            }
        });
    }

    public void imageChooser(){
        Intent i = new Intent();
        i.setType("images/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            Uri imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageViewCircle);
            imageControl = true;
        }else{
            imageControl = false;
        }
    }
}