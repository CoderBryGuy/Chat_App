package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyChatActivity extends AppCompatActivity {

    private ImageView imageViewBack;
    private TextView textViewChat;
    private EditText editText;
    private FloatingActionButton fab;
    private RecyclerView rvChat;

    String userName, otherName;
    FirebaseDatabase database;
    DatabaseReference reference;
    MessageAdapter messageAdapter;
    List<ModelClass> list;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chat);

        imageViewBack = findViewById(R.id.imageView_back_chatAct);
        textViewChat = findViewById(R.id.textView_chatAct);
        editText = findViewById(R.id.editText_chatAct);
        fab = findViewById(R.id.fab_chatAct);
        rvChat = findViewById(R.id.rv_chatAct);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        userName = getIntent().getStringExtra(UsersAdapter.USER_NAME);
        otherName = getIntent().getStringExtra(UsersAdapter.OTHER_NAME);
        textViewChat.setText(otherName);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyChatActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editText.getText().toString();
                if(!message.equals("")){
                    sendMessage(message);
                    editText.setText("");
                }
            }
        });

        getMessage();



    }

    private void getMessage() {
        reference.child("Messages").child(userName).child(otherName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        ModelClass modelClass = snapshot.getValue(ModelClass.class);
                        list.add(modelClass);
                        messageAdapter.notifyDataSetChanged();
                        rvChat.scrollToPosition(list.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messageAdapter = new MessageAdapter(list, userName);
        rvChat.setAdapter(messageAdapter);
    }

    private void sendMessage(String message) {
            String key = reference.child("Messages").child(userName).child(otherName).push().getKey();

            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("message", message);
            messageMap.put("from", userName);

            reference.child("Messages").child(userName).child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        reference.child("Messages").child(otherName).child(userName).child(key).setValue(messageMap);
                    }
                }
            });
    }
}