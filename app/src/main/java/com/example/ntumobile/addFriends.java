package com.example.ntumobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class addFriends extends AppCompatActivity {

    FirebaseRecyclerOptions<user>options;
    FirebaseRecyclerAdapter<user, FindFriendViewHolder>adapter;

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase database;
    DatabaseReference avatarRef;

    private String currentUserID;

    ImageView changeAvatar;

    Button back;

    RecyclerView recyclerView;
    private EditText mSearchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriends);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mUserRef = database.getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        avatarRef = database.getReference("Users");
        currentUserID = mAuth.getCurrentUser().getUid();

        mSearchField = (EditText) findViewById(R.id.search_field);
        back = findViewById(R.id.back_workpg);

        LoadUsers("");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(addFriends.this, work_friends.class);
                startActivity(i);
            }
        });

        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString()!=null)
                {
                    LoadUsers(s.toString());
                }
                else
                {
                    LoadUsers("");
                }


            }
        });


    }

    //Load for Recyclerview

    private void LoadUsers(String s) {

        Query query = mUserRef.orderByChild("name").startAt(s).endAt(s+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<user>().setQuery(query,user.class).build();
        adapter = new FirebaseRecyclerAdapter<user, FindFriendViewHolder>(options) {
            @SuppressLint("RecyclerView")
            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull user model) {
                if (!mUser.getUid().equals(getRef(position).getKey().toString()))
                {
                    //Picasso.get().load(model.getProfileImage()).into(holder.profile_image);
                    holder.course.setText(model.getCourse());
                    holder.Name.setText(model.name);

                    avatarRef.child(currentUserID).child("Avatar Selected").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                if (snapshot!=null)
                                {
                                    String stringAvatarID = snapshot.child("AvatarID").getValue().toString();
                                    int avatarID = Integer.parseInt(stringAvatarID);
                                    changeAvatar= findViewById(R.id.imageView12);
                                    changeAvatar.setImageResource(avatarID);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else
                { //hides user using it
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(addFriends.this,viewFriendProfile.class);
                        intent.putExtra("userKey", getRef(position).getKey().toString());
                        startActivity(intent);
                    }
                });
            }



            @NonNull
            @Override
            public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_layout,parent,false);
                return new FindFriendViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    private class FindFriendViewHolder extends RecyclerView.ViewHolder {

        ImageView profile_image;
        TextView Name, course;

        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image=itemView.findViewById(R.id.imageView12);
            Name = itemView.findViewById(R.id.username);
            course = itemView.findViewById(R.id.status);
        }
    }
}