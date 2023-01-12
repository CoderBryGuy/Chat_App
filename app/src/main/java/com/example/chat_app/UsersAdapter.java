package com.example.chat_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    List<String> userList;
    String userName;
    Context mContext;
    FirebaseDatabase database;
    DatabaseReference reference;
    static final String USER_NAME = "username";
    static final String OTHER_NAME = "othername";


    public UsersAdapter(List<String> userList, String userName, Context mContext) {
        this.userList = userList;
        this.userName = userName;
        this.mContext = mContext;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_card, parent, false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            reference.child("Users").child(userList.get(position)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String otherName = snapshot.child("userName").getValue().toString();
                        String imageURL = snapshot.child("image").getValue().toString();

                        holder.textViewUsers.setText(otherName);

                        if(imageURL.equals("null")){
                            holder.imageViewUsers.setImageResource(R.drawable.account_circle_24);
                        }else{
                            Picasso.get().load(imageURL).into(holder.imageViewUsers);
                        }

                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mContext, MyChatActivity.class);
                                intent.putExtra("userName", userName);
                                intent.putExtra("otherName", otherName);
                                mContext.startActivity(intent);
                            }
                        });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUsers;
        private CircleImageView imageViewUsers;
        private CardView cardView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewUsers = itemView.findViewById(R.id.textView_cardvw);
            imageViewUsers = itemView.findViewById(R.id.circleView_cardvw);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
