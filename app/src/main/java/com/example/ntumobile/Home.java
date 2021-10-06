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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    private Button buttonBooking, buttonCommunity;
    ImageView image1, image2;
    Animation topAnim;

    private String currentUserID; //

    private FirebaseAuth mAuth; //
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase; //
    private DatabaseReference myRef; //
    private FirebaseAuth.AuthStateListener mAuthListener;


    TextView nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation_login);
        image1= (ImageView)findViewById(R.id.home_image1);
        image2= (ImageView)findViewById(R.id.home_image2);

        image1.setAnimation(topAnim);
        image2.setAnimation(topAnim);

        Button buttonBooking = (Button) findViewById(R.id.booking);
        buttonBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBookingPage();
            }
        });

        Button buttonCommunity = (Button) findViewById(R.id.community);
        buttonCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCommunityPage();
            }
        });

        nameText = (TextView) findViewById(R.id.nameText);

        mDatabase= FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        String name = getIntent().getStringExtra("name");
        nameText.setText(name); //Name from Register page instead of database


        currentUserID = mAuth.getCurrentUser().getUid();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    toastMessage("Not logged in");
                }
                // ...
            }
        };

       myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Name = snapshot.child(currentUserID).child("Profile").child("Name").getValue().toString();
                nameText.setText(Name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }

    @Override
    public void onStart () {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop () {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void openBookingPage() {
        Intent intent = new Intent(this, booking_main.class);
        startActivity(intent);
    }
    public void openCommunityPage() {
        Intent intent = new Intent(this, community_main.class);
        startActivity(intent);
    }


    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}