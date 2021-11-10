package com.example.ntumobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class social_cca_su extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_cca_su);

        Button Home = (Button) findViewById(R.id.home_button);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_cca_su.this, Home.class);
                startActivity(intent);
            }
        });

        Button Booking = (Button) findViewById(R.id.booking);
        Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_cca_su.this, booking_main.class);
                startActivity(intent);
            }
        });

        Button Community = (Button) findViewById(R.id.current_community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_cca_su.this, community_main.class);
                startActivity(intent);
            }
        });

        Button buttonChat = (Button) findViewById(R.id.chat_button);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_cca_su.this, chat_grp.class);
                startActivity(intent);
            }
        });

    }
}
