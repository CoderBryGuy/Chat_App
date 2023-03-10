package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView imageViewCircle;
    private TextInputEditText editTextUserNameProfile;
    private Button buttonUpdate;
    private Uri imageUri;
    private boolean imageControl = false;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageViewCircle = findViewById(R.id.imageViewCircle_profile);
        editTextUserNameProfile = findViewById(R.id.editText_username_profile);
        buttonUpdate = findViewById(R.id.button_profile);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        getUserInfo();

        imageViewCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        updateProfile();
            }
        });
    }

    private void imageChooser() {
        Intent i = new Intent();
        i.setType("images/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageViewCircle);
            imageControl = true;
        }else{
            imageControl = false;
        }
    }

    public void updateProfile(){
        String userName = editTextUserNameProfile.getText().toString();
        reference.child("Users").child(firebaseUser.getUid()).child("userName").setValue(userName);

        if(imageControl){
            UUID randomID = UUID.randomUUID();
            String imageName = "image/"+randomID+".jpg";
            storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    StorageReference myStorageRef = firebaseStorage.getReference(imageName);

                    myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {

                            String filePath = uri.toString();

                            reference.child("Users").child(auth.getUid()).child("image").setValue(filePath)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                                        @Override
                                        public void onSuccess(Void unused) {

                                            Toast.makeText(ProfileActivity.this, "Write to database is successful.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {

                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(ProfileActivity.this, "Write to database is not successful.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                }
            });
        }else{
            reference.child("Users").child(auth.getUid()).child("image").setValue(image);
        }

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        intent.putExtra("userName", userName);
        startActivity(intent);
        finish();
    }

    public void getUserInfo(){
        reference.child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("userName").getValue().toString();
                image = snapshot.child("image").getValue().toString();
                editTextUserNameProfile.setText(name);

                if(image.equals("null")){
                    imageViewCircle.setImageResource(R.drawable.account_circle_24);
                }else{
                    Picasso.get().load(image).into(imageViewCircle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}