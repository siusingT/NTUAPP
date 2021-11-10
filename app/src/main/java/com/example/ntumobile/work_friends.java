package com.example.ntumobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class work_friends extends AppCompatActivity {

    FirebaseRecyclerOptions<Friends>options;
    FirebaseRecyclerAdapter<Friends,FriendViewHolder>adapter;

    Button btnAddFriend, back;
    RecyclerView friendList;

    DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase database;
    DatabaseReference avatarRef;

    private String currentUserID, myUid;

    ImageView changeAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_friends);

        Button Home = (Button) findViewById(R.id.home_button);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(work_friends.this, Home.class);
                startActivity(intent);
            }
        });

        Button Booking = (Button) findViewById(R.id.booking);
        Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(work_friends.this, booking_main.class);
                startActivity(intent);
            }
        });

        Button Community = (Button) findViewById(R.id.current_community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(work_friends.this, community_main.class);
                startActivity(intent);
            }
        });

        Button buttonChat = (Button) findViewById(R.id.chat_button);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(work_friends.this, chat_grp.class);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mUserRef = database.getReference().child("Friends");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        avatarRef = database.getReference("Users");
        currentUserID = mAuth.getCurrentUser().getUid();

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
                //String hisUid = getIntent().getStringExtra("hisUid");
                //Friends modelFriends = friendList.get(position);
                //String hisUid = model.getUserID();
                String hisUid = getRef(position).getKey();
                String name = avatarRef.child(currentUserID).child("name").toString();

                avatarRef.child(currentUserID).child("Avatar Selected").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (snapshot != null) {
                                String stringAvatarID = snapshot.child("AvatarID").getValue().toString();
                                int avatarID = Integer.parseInt(stringAvatarID);
                                changeAvatar= findViewById(R.id.imageView12);
                                changeAvatar.setImageResource(avatarID);

                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        CharSequence options[] = new CharSequence[]
                                                {
                                                        //name + "'s Profile",
                                                        "Send Message"
                                                };
                                        AlertDialog.Builder builder = new AlertDialog.Builder(work_friends.this);
                                        builder.setTitle("Select Option");

                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int which) {
                                                /*if (which==0){
                                                    Intent profileIntent = new Intent(work_friends.this, viewFriendProfile.class);
                                                    profileIntent.putExtra("Users", userID);
                                                    startActivity(profileIntent);
                                                }*/
                                                if (which==0){
                                                    Intent chatIntent = new Intent(work_friends.this, ChatActivity.class);
                                                    chatIntent.putExtra("hisUid", hisUid);
                                                    //dbRef.child(myUid).child("sender").setValue(hisUid);
                                                    startActivity(chatIntent);
                                                }
                                            }
                                        });
                                        builder.show();
                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

        ImageView profile_image;
        TextView Name, bio;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image=itemView.findViewById(R.id.imageView12);
            Name = itemView.findViewById(R.id.username);
            bio = itemView.findViewById(R.id.status);

        }
    }
}