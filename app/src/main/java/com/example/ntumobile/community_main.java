package com.example.ntumobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class community_main extends AppCompatActivity {

    Button home, booking;
    ImageView communityImage;
    Animation topAnim;

    Button Social, Work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);


        ImageView communityImage = (ImageView)findViewById(R.id.community_image);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation_login);
        communityImage.setAnimation(topAnim);

        //navigation bar
        Button Home = (Button) findViewById(R.id.home_button);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(community_main.this, Home.class);
                startActivity(intent);
            }
        });

        Button Booking = (Button) findViewById(R.id.booking);
        Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(community_main.this, booking_main.class);
                startActivity(intent);
            }
        });

        Button buttonChat = (Button) findViewById(R.id.chat_button);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(community_main.this, chat_main.class);
                startActivity(intent);
            }
        });


        //on the tab
        Social = (Button) findViewById(R.id.social);
        Social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(community_main.this, social_main.class);
                startActivity(intent);

            }
        });

        Work = (Button) findViewById(R.id.work);
        Work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(community_main.this, work_main.class);
                startActivity(intent);
            }
        });

    }


}