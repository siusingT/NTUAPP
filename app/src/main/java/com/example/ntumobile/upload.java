package com.example.ntumobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class upload extends Activity {

    private Button fileB, textB, imageB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popout_upload);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.widthPixels;

        getWindow().setLayout((int)(width),(int)(height));

        fileB = (Button) findViewById(R.id.btnFile);
        fileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(upload.this, uploadPDFactivity.class);
                startActivity(back);
            }
        });

        /*textB = (Button) findViewById(R.id.btnText);
        textB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(upload.this, .class);
                startActivity(back);
            }
        });*/

        imageB = (Button) findViewById(R.id.btnImage);
        imageB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(upload.this, uploadImage.class);
                startActivity(back);
            }
        });
    }

}
