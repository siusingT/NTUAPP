package com.example.ntumobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class social_jio_sports_createSession extends AppCompatActivity {

    private Button mCreateSession;

    private EditText mNewActivity, mNewLocation, mNewTime,mNewPax,mCurrPax;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    int count,count_session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_jio_sports_createsession);

        mCreateSession = (Button) findViewById(R.id.createsession_btn);
        mNewActivity = (EditText) findViewById(R.id.activity_et);
        mNewLocation = (EditText) findViewById(R.id.location_et);
        mNewTime = (EditText) findViewById(R.id.time_et);
        mNewPax = findViewById(R.id.pax_et);

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        // Read from the database
        myRef.child("Session").addValueEventListener(new ValueEventListener() {
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

        // Read from the database
        myRef.child(userID).child("SessionInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.exists()){
                    count_session = (int)dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                toastMessage("Error");
            }
        });

        mCreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countt = String.valueOf(count);
                String countt_session = String.valueOf(count_session);
                String newActivity = mNewActivity.getText().toString();
                String newLocation = mNewLocation.getText().toString();
                String newTime = mNewTime.getText().toString();
                String newPax = mNewPax.getText().toString();
                String currPax = "1";

                if(!newActivity.equals("")){
                    myRef.child("Session").child(countt).child("Activity").setValue(newActivity);
                    //reset the text
                    mNewActivity.setText("");
                }
                if(!newLocation.equals("")){
                    myRef.child("Session").child(countt).child("Location").setValue(newLocation);
                    //reset the text
                    mNewLocation.setText("");
                }
                if(!newTime.equals("")){
                    myRef.child("Session").child(countt).child("Time").setValue(newTime);
                    //reset the text
                    mNewTime.setText("");
                }
                if(!newPax.equals("")){
                    myRef.child("Session").child(countt).child("Pax").setValue(newPax);
                    //reset the text
                    mNewPax.setText("");
                }

                myRef.child("Session").child(countt).child("CurrPax").setValue(currPax);

                toastMessage("Session Created");
                Intent intent = new Intent(social_jio_sports_createSession.this, social_jio_sports.class);
                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();
                myRef.child(userID).child("SessionInfo").child(countt_session).child("Activity").setValue(newActivity);
                myRef.child(userID).child("SessionInfo").child(countt_session).child("Location").setValue(newLocation);
                myRef.child(userID).child("SessionInfo").child(countt_session).child("Time").setValue(newTime);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //add a toast to show when successfully signed in


    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}