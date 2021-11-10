package com.example.ntumobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

import java.util.Calendar;
import java.util.Locale;

public class Home extends AppCompatActivity {

    Button buttonBooking;
    TextView date, tr, timeslot;
    ImageButton friend,profile,chat;

    private String currentUserID; //

    private FirebaseAuth mAuth; //
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase; //
    private DatabaseReference myRef, avatarRef; //
    private FirebaseAuth.AuthStateListener mAuthListener;

    TextView nameText;
    ImageView changeAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //bottom navigation
        Button buttonBooking = (Button) findViewById(R.id.booking);
        buttonBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, booking_main.class);
                startActivity(intent);
            }
        });

        ImageButton buttonCommunity = (ImageButton) findViewById(R.id.community);
        buttonCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, community_main.class);
                startActivity(intent);
            }
        });

        ImageButton buttonChat = (ImageButton) findViewById(R.id.chat_button);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, chat_grp.class);
                startActivity(intent);
            }
        });

        nameText = (TextView) findViewById(R.id.nameText);
        date = (TextView) findViewById(R.id.date);
        tr = (TextView) findViewById(R.id.tr);
        timeslot = (TextView) findViewById(R.id.timeslot);

        friend = (ImageButton)findViewById(R.id.friends);
        chat = (ImageButton)findViewById(R.id.menu_chat);
        profile =(ImageButton)findViewById(R.id.profile_down);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(Home.this, profile.class);
                startActivity(profile);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);//fading animation to profile page
            }
        });

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friend = new Intent(Home.this,work_friends.class);
                startActivity(friend);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chat = new Intent(Home.this,chat_grp.class );
                startActivity(chat);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });


        mDatabase= FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = mDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        String name = getIntent().getStringExtra("name");
        nameText.setText(name); //Name from Register page instead of database



        currentUserID = mAuth.getCurrentUser().getUid();

        avatarRef = mDatabase.getReference("Users");
        avatarRef .child(currentUserID).child("userID").setValue(currentUserID);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        String dateTime = "Last seen at " + DateFormat.format("dd/MM/yyyy hh:mm aa", cal).toString();
        avatarRef.child(currentUserID).child("onlineStatus").setValue(dateTime);

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

                if(snapshot.child("Users").child(currentUserID).child("Room").exists()){
                    String Name = snapshot.child("Users").child(currentUserID).child("name").getValue().toString();
                    String TR = snapshot.child("Users").child(currentUserID).child("Room").child("0").child("TR").getValue().toString();
                    String TimeSlot = snapshot.child("Users").child(currentUserID).child("Room").child("0").child("Time").getValue().toString();
                    String Date = snapshot.child("Users").child(currentUserID).child("Room").child("0").child("Date").getValue().toString();
                    nameText.setText(Name);
                    tr.setText(TR);
                    timeslot.setText(TimeSlot);
                    date.setText(Date);
                }
                else{
                    String Name = snapshot.child("Users").child(currentUserID).child("name").getValue().toString();
                    nameText.setText(Name);
                    date.setText("NoBookings");
                }

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

    public void openActivityLogin() {
        Intent intent = new Intent(this, booking_main.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}