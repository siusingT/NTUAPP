package com.example.ntumobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class work_friends extends AppCompatActivity {

    FirebaseRecyclerOptions<Friends>options;
    FirebaseRecyclerAdapter<Friends,FriendViewHolder>adapter;

    Button btnAddFriend, back;
    RecyclerView friendList;

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_friends);

        database = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mUserRef = database.getReference().child("Friends");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        btnAddFriend = findViewById(R.id.addfriend);
        back = findViewById(R.id.back_workpg);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(work_friends.this, Home.class);
                startActivity(i);
            }
        });
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(work_friends.this, addFriends.class);
                startActivity(intent);
            }
        });

        friendList=findViewById(R.id.result_list);
        friendList.setLayoutManager(new LinearLayoutManager(this));

        LoadUsers("");



    }

    private void LoadUsers(String s) {

        Query query = mUserRef.child(mUser.getUid());
        options = new FirebaseRecyclerOptions.Builder<Friends>().setQuery(query,Friends.class).build();
        adapter = new FirebaseRecyclerAdapter<Friends, FriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FriendViewHolder holder, int position, @NonNull Friends model) {
                //Picasso.get().load(model.getProfileImageUrl()).into(holder.profile_image);
                holder.Name.setText(model.name);
                //holder.bio.setText(model.getStatus());
            }

            @NonNull
            @Override
            public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_layout,parent,false);

                return new FriendViewHolder(view);
            }
        };
        adapter.startListening();
        friendList.setAdapter(adapter);

    }

    private class FriendViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profile_image;
        TextView Name, bio;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image=itemView.findViewById(R.id.profile_image);
            Name = itemView.findViewById(R.id.username);
            bio = itemView.findViewById(R.id.status);

        }
    }
}