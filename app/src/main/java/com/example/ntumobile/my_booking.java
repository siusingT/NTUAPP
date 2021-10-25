package com.example.ntumobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class my_booking extends AppCompatActivity {

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;

    //private ListView mListView;
    private RecyclerView recyclerView;
    ArrayList<booking_display> list;
    private booking_adapter adapter;

    int count;

    String countt;

    Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_booking);


        //mListView = (ListView) findViewById(R.id.bookingList_tv);

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be usable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        //myRef = mFirebaseDatabase.getReference().child("Users").child(userID).child("Room");
        myRef = mFirebaseDatabase.getReference().child("Users").child(userID).child("Room");

        count = getIntent().getIntExtra("countt", 0);
        countt = String.valueOf(count);

        Button back =(Button) findViewById(R.id.back_bookInfo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(my_booking.this, booking_main.class);
                count++;
                back.putExtra("countt", count);
                startActivity(back);
            }
        });



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                   // Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };

        recyclerView = findViewById(R.id.recycler2);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));


        list = new ArrayList<>();
        adapter = new booking_adapter(this,list);
        recyclerView.setAdapter(adapter);
        //adapter.setOnItemClickListener(my_booking.this);

        //final ArrayList<String> list = new ArrayList<>();
        //final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        //mListView.setAdapter(adapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                /*list.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    list.add(ds.getValue().toString());
                }*/

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    booking_display session = dataSnapshot.getValue(booking_display.class);
                    list.add(session);


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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


    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}