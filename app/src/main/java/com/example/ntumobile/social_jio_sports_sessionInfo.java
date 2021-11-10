package com.example.ntumobile;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class social_jio_sports_sessionInfo extends AppCompatActivity {
    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private TextView activityInfo,locationInfo,timeInfo,currPax,maxPax;
    private Button join_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_jio_sports_sessioninfo);

        Button back = (Button)findViewById(R.id.back_sports);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(social_jio_sports_sessionInfo.this, social_jio_sports.class);
                startActivity(back);
            }
        });

        Button goToChat = (Button)findViewById(R.id.chatSession_btn);
        goToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(social_jio_sports_sessionInfo.this, chat_grp.class);
                startActivity(back);
            }
        });

        activityInfo = findViewById(R.id.activityInfo_tv);
        locationInfo = findViewById(R.id.locationInfo_tv);
        timeInfo = findViewById(R.id.timeInfo_tv);
        currPax = findViewById(R.id.CurrPax);
        maxPax = findViewById(R.id.MaxPax);
        join_btn = findViewById(R.id.joinSession_btn);

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mAuth = FirebaseAuth.getInstance();


        int count = getIntent().getIntExtra("sessionCount",0);
        int data = getIntent().getIntExtra("sessionID",0); // retrieve position from JoinSession OnClickPixel

        myRef = mFirebaseDatabase.getReference();

        myRef.child("Session").child(String.valueOf(data)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                String activity = ds.child("Activity").getValue().toString();
                String location = ds.child("Location").getValue().toString();
                String time = ds.child("Time").getValue().toString();
                String pax = ds.child("Pax").getValue().toString();
                String currpax = ds.child("CurrPax").getValue().toString();

                // updating info of activity after user clicks
                activityInfo.setText(activity);
                locationInfo.setText(location);
                timeInfo.setText(time);
                maxPax.setText(pax);
                currPax.setText(currpax);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot ds) {
                        String countt = String.valueOf(count);

                        // session info
                        String activity = ds.child("Session").child(String.valueOf(data)).child("Activity").getValue().toString();
                        String location = ds.child("Session").child(String.valueOf(data)).child("Location").getValue().toString();
                        String time = ds.child("Session").child(String.valueOf(data)).child("Time").getValue().toString();
                        String pax = ds.child("Session").child(String.valueOf(data)).child("Pax").getValue().toString();
                        String currpax = ds.child("Session").child(String.valueOf(data)).child("CurrPax").getValue().toString();

                        FirebaseUser user = mAuth.getCurrentUser();
                        String userID = user.getUid();
                        int newCurrPax = Integer.parseInt(currpax);
                        int maxPax = Integer.parseInt(pax);
                        boolean isNotJoined = true;

                        // iterate through SessionInfo and compare with Session
                        for (int i=0; i<=count-1; i++){
                            String joinedActivity = ds.child(userID).child("SessionInfo").child(String.valueOf(i)).child("Activity").getValue().toString();
                            String joinedLocation = ds.child(userID).child("SessionInfo").child(String.valueOf(i)).child("Location").getValue().toString();
                            String joinedTime = ds.child(userID).child("SessionInfo").child(String.valueOf(i)).child("Time").getValue().toString();
                            if (activity.equalsIgnoreCase(joinedActivity) && location.equalsIgnoreCase(joinedLocation) && time.equalsIgnoreCase(joinedTime)){
                                toastMessage("Already joined!");
                                isNotJoined = false;
                            }
                        }

                        if (newCurrPax < maxPax && isNotJoined) { // check if slots available
                            newCurrPax += 1;
                            currPax.setText(String.valueOf(newCurrPax));
                            myRef.child("Session").child(String.valueOf(data)).child("CurrPax").setValue(String.valueOf(newCurrPax));
                            myRef.child(userID).child("SessionInfo").child(countt).child("Activity").setValue(activity);
                            myRef.child(userID).child("SessionInfo").child(countt).child("Location").setValue(location);
                            myRef.child(userID).child("SessionInfo").child(countt).child("Time").setValue(time);
                            toastMessage("Successfully joined");
                            Intent intent = new Intent(social_jio_sports_sessionInfo.this, social_jio_sports.class);
                            startActivity(intent);

                        } else if (newCurrPax >= maxPax && isNotJoined) {
                            toastMessage("Session is full!");
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}