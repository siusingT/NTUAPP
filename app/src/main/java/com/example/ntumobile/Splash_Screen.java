package com.example.ntumobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash_Screen extends AppCompatActivity {

    Button button1;
    Animation topAnim, bottomAnim;
    ImageView image1,image2;
    TextView text1, text2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.buttom_animation);

        //Hooks
        image1 = (ImageView)findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.buttonImage);
        text1 = (TextView) findViewById(R.id.splash_text1);
        text2 = (TextView) findViewById(R.id.splash_text2);

        image1.setAnimation(topAnim);
        image2.setAnimation(bottomAnim);
        text1.setAnimation(bottomAnim);
        text2.setAnimation(bottomAnim);

        Button button1 = (Button) findViewById(R.id.start_button);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityLogin();
            }
        });

    }

    public void openActivityLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }


}