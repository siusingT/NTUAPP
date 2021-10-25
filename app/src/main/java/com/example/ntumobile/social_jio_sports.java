package com.example.ntumobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class social_jio_sports extends AppCompatActivity implements social_jio_sports_sessionAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private social_jio_sports_sessionAdapter adapter; // Create Object of the Adapter class
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ArrayList<social_jio_sports_session> list;
    private Button create_btn;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_jio_sports);

        mFirebaseDatabase = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        create_btn = findViewById(R.id.selectcreate_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(social_jio_sports.this, social_jio_sports_createSession.class);
                startActivity(intent);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        recyclerView = findViewById(R.id.recycler1);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));


        list = new ArrayList<>();
        adapter = new social_jio_sports_sessionAdapter(this,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(social_jio_sports.this);

        // Read from the database
        myRef.child(userID).child("SessionInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists()){
                    count = (int)dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                toastMessage("Error");
            }
        });

        myRef.child("Session").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    social_jio_sports_session session = dataSnapshot.getValue(social_jio_sports_session.class);
                    list.add(session);


                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Bottom bar navigation
        Button Home = (Button) findViewById(R.id.home_button);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_jio_sports.this, Home.class);
                startActivity(intent);
            }
        });

        Button Booking = (Button) findViewById(R.id.booking);
        Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_jio_sports.this, booking_main.class);
                startActivity(intent);
            }
        });

        Button Community = (Button) findViewById(R.id.current_community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_jio_sports.this, community_main.class);
                startActivity(intent);
            }
        });

        Button buttonChat = (Button) findViewById(R.id.chat_button);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_jio_sports.this, chat_main.class);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onItemClick(int position) {
        FirebaseUser user = mAuth.getCurrentUser();
        String countt = String.valueOf(count);
        String userID = user.getUid();
        Intent intent = new Intent(social_jio_sports.this, social_jio_sports_sessionInfo.class);
        intent.putExtra("sessionID", position);  // pass your values and retrieve them in the other Activity using keyName
        intent.putExtra("sessionCount", count);  // pass your values and retrieve them in the other Activity using keyName
        startActivity(intent);
    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
