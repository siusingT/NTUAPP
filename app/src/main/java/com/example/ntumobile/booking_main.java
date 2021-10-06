package com.example.ntumobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class booking_main extends AppCompatActivity {

    Button home;
    ImageView bookingImage;
    Animation topAnim;

    Button Hive, Arc, Sport;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    String userID;

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_main);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        count = getIntent().getIntExtra("countt", 0);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                userID = user.getUid();
                if (user != null) {
                    // User is signed in
                    //toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    //toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

        myRef.child(userID).child("Room").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.exists()){
                    count = (int)dataSnapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                toastMessage("Error");
            }
        });


        ImageView bookingImage = (ImageView)findViewById(R.id.booking_image);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation_login);
        bookingImage.setAnimation(topAnim);

        Button Home = (Button) findViewById(R.id.home_button);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(booking_main.this, Home.class);
                startActivity(intent);
            }
        });

        Button Community = (Button) findViewById(R.id.community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(booking_main.this, community_main.class);
                startActivity(intent);
            }
        });


        Hive = (Button) findViewById(R.id.hive);
        Hive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String countt = String.valueOf(count);
                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();
                myRef.child(userID).child("Room").child(countt).child("Location").setValue("Hive");
                toastMessage("Selected Hive");
                Intent intent = new Intent(booking_main.this, booking_hive.class);
                intent.putExtra("countt", count); //pass values and retrieve in other activities using keyName
                startActivity(intent);

            }
        });

        Arc = (Button) findViewById(R.id.Arc);
        Arc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countt = String.valueOf(count);
                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();
                myRef.child(userID).child("Room").child(countt).child("Location").setValue("Arc");
                toastMessage("Selected Arc");
                Intent intent = new Intent(booking_main.this, booking_hive.class);
                intent.putExtra("countt", count); //pass values and retrieve in other activities using keyName
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



    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}