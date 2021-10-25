package com.example.ntumobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class booking_timeslot extends AppCompatActivity {

    Button back;
    ImageButton book;
    Button time1, time2,time3, time4,time5,time6;
    String timeslot = "Not selected";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    int count, counterUp;
    String countt, countUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_timeslot);

        time1 = (Button)findViewById(R.id.time1);
        time2 = (Button)findViewById(R.id.time2);
        time3 = (Button)findViewById(R.id.time3);
        time4 = (Button)findViewById(R.id.time4);
        time5 = (Button)findViewById(R.id.time5);
        time6 = (Button)findViewById(R.id.time6);

        count = getIntent().getIntExtra("countt", 0);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = mFirebaseDatabase.getReference().child("Users");
        //myRef = mFirebaseDatabase.getReference().child("Users");
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();



        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time1.setBackgroundColor(Color.parseColor("#6979e9"));
                time2.setBackgroundColor(Color.parseColor("#92c1ff"));
                time3.setBackgroundColor(Color.parseColor("#92c1ff"));
                time4.setBackgroundColor(Color.parseColor("#92c1ff"));
                time5.setBackgroundColor(Color.parseColor("#92c1ff"));
                time6.setBackgroundColor(Color.parseColor("#92c1ff"));

                timeslot = "09:00-11:00";
            }
        });

        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time2.setBackgroundColor(Color.parseColor("#6979e9"));
                time1.setBackgroundColor(Color.parseColor("#92c1ff"));
                time3.setBackgroundColor(Color.parseColor("#92c1ff"));
                time4.setBackgroundColor(Color.parseColor("#92c1ff"));
                time5.setBackgroundColor(Color.parseColor("#92c1ff"));
                time6.setBackgroundColor(Color.parseColor("#92c1ff"));

                timeslot = "11:00-13:00";
            }
        });

        time3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time3.setBackgroundColor(Color.parseColor("#6979e9"));
                time1.setBackgroundColor(Color.parseColor("#92c1ff"));
                time2.setBackgroundColor(Color.parseColor("#92c1ff"));
                time4.setBackgroundColor(Color.parseColor("#92c1ff"));
                time5.setBackgroundColor(Color.parseColor("#92c1ff"));
                time6.setBackgroundColor(Color.parseColor("#92c1ff"));

                timeslot = "13:00-15:00";
            }
        });

        time4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time4.setBackgroundColor(Color.parseColor("#6979e9"));
                time1.setBackgroundColor(Color.parseColor("#92c1ff"));
                time2.setBackgroundColor(Color.parseColor("#92c1ff"));
                time3.setBackgroundColor(Color.parseColor("#92c1ff"));
                time5.setBackgroundColor(Color.parseColor("#92c1ff"));
                time6.setBackgroundColor(Color.parseColor("#92c1ff"));

                timeslot = "15:00-17:00";
            }
        });

        time5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time5.setBackgroundColor(Color.parseColor("#6979e9"));
                time1.setBackgroundColor(Color.parseColor("#92c1ff"));
                time3.setBackgroundColor(Color.parseColor("#92c1ff"));
                time4.setBackgroundColor(Color.parseColor("#92c1ff"));
                time2.setBackgroundColor(Color.parseColor("#92c1ff"));
                time6.setBackgroundColor(Color.parseColor("#92c1ff"));

                timeslot = "17:00-19:00";
            }
        });

        time6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time6.setBackgroundColor(Color.parseColor("#6979e9"));
                time1.setBackgroundColor(Color.parseColor("#92c1ff"));
                time3.setBackgroundColor(Color.parseColor("#92c1ff"));
                time4.setBackgroundColor(Color.parseColor("#92c1ff"));
                time5.setBackgroundColor(Color.parseColor("#92c1ff"));
                time2.setBackgroundColor(Color.parseColor("#92c1ff"));

                timeslot = "19:00-:21:00";
            }
        });




        Button back = (Button) findViewById(R.id.back_bookInfo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(booking_timeslot.this, booking_hive.class);
                startActivity(back);
            }
        });

        ImageButton book = (ImageButton) findViewById(R.id.book_button);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timeslot.equals("Not Selected")){
                        toastMessage("Not selected");
                }else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    countt = String.valueOf(count);
                    myRef.child(userID).child("Room").child(countt).child("Time").setValue(timeslot);

                    Intent book = new Intent(booking_timeslot.this,my_booking.class);
                    book.putExtra("countt",count);
                    startActivity(book);
                }
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}