package com.example.ntumobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    ImageButton back_home, logoutBtn, editProfile;
    ImageView img1, img2;
    Animation topAnim;

    private String currentUserID; //

    private FirebaseAuth mAuth; //
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase; //
    private DatabaseReference myRef; //
    private FirebaseAuth.AuthStateListener mAuthListener;
    TextView nameText,email,school,course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        img1 = (ImageView)findViewById(R.id.img1);
        img2 =(ImageView)findViewById(R.id.img2);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation_login);
        img1.setAnimation(topAnim);
        img2.setAnimation(topAnim);

        back_home= (ImageButton) findViewById(R.id.back_home);
        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome = new Intent(profile.this, Home.class);
                startActivity(backHome);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        logoutBtn= (ImageButton) findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginPg = new Intent(profile.this, Login.class);
                startActivity(loginPg);
            }
        });

        editProfile= (ImageButton) findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent avatarpg = new Intent(profile.this, avatar.class);
                startActivity(avatarpg);
            }
        });

        nameText = (TextView)findViewById(R.id.nameText);
        email = (TextView)findViewById(R.id.get_email);
        school = (TextView)findViewById(R.id.get_school);
        course = (TextView)findViewById(R.id.get_course);

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
                    //toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    //toastMessage("Not logged in");
                }
                // ...
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Name = snapshot.child("Users").child(currentUserID).child("name").getValue().toString();
                String School = snapshot.child("Users").child(currentUserID).child("school").getValue().toString();
                String Course = snapshot.child("Users").child(currentUserID).child("course").getValue().toString();
                String Email = snapshot.child("Users").child(currentUserID).child("email").getValue().toString();
                nameText.setText(Name);
                school.setText(School);
                course.setText(Course);
                email.setText(Email);

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
}