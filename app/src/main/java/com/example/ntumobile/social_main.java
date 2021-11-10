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

public class social_main extends AppCompatActivity {

    Button home, booking;
    ImageView socialImage;
    Animation topAnim;

    Button Cca, Jio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_main);


        ImageView socialImage = (ImageView)findViewById(R.id.social_image);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation_login);
        socialImage.setAnimation(topAnim);

        Button Home = (Button) findViewById(R.id.home_button);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_main.this, Home.class);
                startActivity(intent);
            }
        });

        Button Booking = (Button) findViewById(R.id.booking);
        Booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_main.this, booking_main.class);
                startActivity(intent);
            }
        });

        Button buttonChat = (Button) findViewById(R.id.chat_button);
        buttonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(social_main.this, chat_grp.class);
                startActivity(intent);
            }
        });



        Cca = (Button) findViewById(R.id.cca);
        Cca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(social_main.this, social_cca.class);
                startActivity(intent);

            }
        });

        Jio = (Button) findViewById(R.id.jio);
        Jio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(social_main.this, social_jio.class);
                startActivity(intent);
            }
        });


    }


}