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

public class social_cca extends AppCompatActivity {

    Button home, booking;
    ImageView cca_image;
    Animation topAnim;

    Button su;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_cca);


        ImageView ccaImage = (ImageView)findViewById(R.id.cca_image);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation_login);
        ccaImage.setAnimation(topAnim);

        Button su = (Button) findViewById(R.id.cca_su);
        su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_cca.this, social_cca_su.class);
                startActivity(intent);
            }
        });

        Button Home = (Button) findViewById(R.id.home_button);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_cca.this, Home.class);
                startActivity(intent);
            }
        });

        Button Booking = (Button) findViewById(R.id.booking);
        Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_cca.this, booking_main.class);
                startActivity(intent);
            }
        });

        Button Community = (Button) findViewById(R.id.current_community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_cca.this, community_main.class);
                startActivity(intent);
            }
        });

        Button buttonChat = (Button) findViewById(R.id.chat_button);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_cca.this, chat_main.class);
                startActivity(intent);
            }
        });

    }


}