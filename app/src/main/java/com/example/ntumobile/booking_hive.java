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
import com.harrywhewell.scrolldatepicker.DayScrollDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class booking_hive extends AppCompatActivity {

    Button back, home;
    Button tr12, tr13, tr14,tr15;
    ImageButton table1, table2, table3,table4,table5;
    DayScrollDatePicker datePicker;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    int count;
    String TR = "Not selected";
    String dateSelected = "Not selected";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_hive);


        Button back = (Button)findViewById(R.id.back_bookInfo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(booking_hive.this, booking_main.class);
                startActivity(back);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        count = getIntent().getIntExtra("countt", 0);

        tr12 = (Button)findViewById(R.id.time1);
        tr13 = (Button)findViewById(R.id.tr13);
        tr14 = (Button)findViewById(R.id.tr14);
        tr15 = (Button)findViewById(R.id.tr15);

        tr12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    tr12.setBackgroundColor(Color.parseColor("#6979e9"));
                    tr13.setBackgroundColor(Color.parseColor("#92c1ff"));
                    tr14.setBackgroundColor(Color.parseColor("#92c1ff"));
                    tr15.setBackgroundColor(Color.parseColor("#92c1ff"));


                /*FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();
                String countt = String.valueOf(count);
                myRef.child(userID).child("Room").child(countt).child("TR").setValue("12");*/
                TR = "TR12";
            }
        });

        tr13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tr13.setBackgroundColor(Color.parseColor("#6979e9"));
                tr12.setBackgroundColor(Color.parseColor("#92c1ff"));
                tr14.setBackgroundColor(Color.parseColor("#92c1ff"));
                tr15.setBackgroundColor(Color.parseColor("#92c1ff"));


                /*FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();
                String countt = String.valueOf(count);
                myRef.child(userID).child("Room").child(countt).child("TR").setValue("13");*/
                TR = "TR13";
            }
        });

        tr14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tr14.setBackgroundColor(Color.parseColor("#6979e9"));
                tr12.setBackgroundColor(Color.parseColor("#92c1ff"));
                tr13.setBackgroundColor(Color.parseColor("#92c1ff"));
                tr15.setBackgroundColor(Color.parseColor("#92c1ff"));


                /*FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();
                String countt = String.valueOf(count);
                myRef.child(userID).child("Room").child(countt).child("TR").setValue("14");*/
                TR = "TR14";
            }
        });

        tr15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tr15.setBackgroundColor(Color.parseColor("#6979e9"));
                tr12.setBackgroundColor(Color.parseColor("#92c1ff"));
                tr13.setBackgroundColor(Color.parseColor("#92c1ff"));
                tr14.setBackgroundColor(Color.parseColor("#92c1ff"));


                /*FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();
                String countt = String.valueOf(count);
                myRef.child(userID).child("Room").child(countt).child("TR").setValue("15");*/
                TR = "TR15";
            }
        });







        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.date_picker)
                .range(startDate, endDate)
                .datesNumberOnScreen(6)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Calendar cal = horizontalCalendar.getDateAt(position);

                cal.add(Calendar.DATE,1);
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                String formatted = format1.format(cal.getTime());

                dateSelected = formatted;
                toastMessage(formatted + " Selected");
            }
        });




        ImageButton table1 = (ImageButton) findViewById(R.id.table1);
        ImageButton table2 = (ImageButton) findViewById(R.id.table2);
        ImageButton table3 = (ImageButton) findViewById(R.id.table3);
        ImageButton table4 = (ImageButton) findViewById(R.id.table4);
        ImageButton table5 = (ImageButton) findViewById(R.id.table5);

        table1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TR.equals("Not Selected") ){
                    toastMessage("please select the TR");
                }
                else if(dateSelected.equals("Not Selected")){
                    toastMessage("please select the date");
                }
                else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    String countt = String.valueOf(count);
                    myRef.child(userID).child("Room").child(countt).child("TR").setValue(TR);
                    myRef.child(userID).child("Room").child(countt).child("Date").setValue(dateSelected);
                    Intent table = new Intent(booking_hive.this,booking_timeslot.class);
                    table.putExtra("countt",count);
                    startActivity(table);
                }

            }
        });

        table2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TR.equals("Not Selected") ){
                    toastMessage("please select the TR");
                }
                else if(dateSelected.equals("Not Selected")){
                    toastMessage("please select the date");
                }
                else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    String countt = String.valueOf(count);
                    myRef.child(userID).child("Room").child(countt).child("TR").setValue(TR);
                    myRef.child(userID).child("Room").child(countt).child("Date").setValue(dateSelected);
                    Intent table = new Intent(booking_hive.this,booking_timeslot.class);
                    table.putExtra("countt",count);
                    startActivity(table);
                }
            }
        });

        table3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TR.equals("Not Selected") ){
                    toastMessage("please select the TR");
                }
                else if(dateSelected.equals("Not Selected")){
                    toastMessage("please select the date");
                }
                else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    String countt = String.valueOf(count);
                    myRef.child(userID).child("Room").child(countt).child("TR").setValue(TR);
                    myRef.child(userID).child("Room").child(countt).child("Date").setValue(dateSelected);
                    Intent table = new Intent(booking_hive.this,booking_timeslot.class);
                    table.putExtra("countt",count);
                    startActivity(table);
                }
            }
        });

        table4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TR.equals("Not Selected") ){
                    toastMessage("please select the TR");
                }
                else if(dateSelected.equals("Not Selected")){
                    toastMessage("please select the date");
                }
                else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    String countt = String.valueOf(count);
                    myRef.child(userID).child("Room").child(countt).child("TR").setValue(TR);
                    myRef.child(userID).child("Room").child(countt).child("Date").setValue(dateSelected);
                    Intent table = new Intent(booking_hive.this,booking_timeslot.class);
                    table.putExtra("countt",count);
                    startActivity(table);
                }
            }
        });

        table5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TR.equals("Not Selected") ){
                    toastMessage("please select the TR");
                }
                else if(dateSelected.equals("Not Selected")){
                    toastMessage("please select the date");
                }
                else{
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    String countt = String.valueOf(count);
                    myRef.child(userID).child("Room").child(countt).child("TR").setValue(TR);
                    myRef.child(userID).child("Room").child(countt).child("Date").setValue(dateSelected);
                    Intent table = new Intent(booking_hive.this,booking_timeslot.class);
                    table.putExtra("countt",count);
                    startActivity(table);
                }
            }
        });

    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}