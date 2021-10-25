package com.example.ntumobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.util.ArrayList;

public class work_specificmod_notes extends AppCompatActivity {


    private Button uploaditems, backButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_specificmod_notes);

        uploaditems = (Button) findViewById(R.id.upload);
        uploaditems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upload = new Intent(work_specificmod_notes.this, upload.class);
                startActivity(upload);
            }
        });


        backButton = (Button) findViewById(R.id.back_specfmodpg);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(work_specificmod_notes.this, work_specificmod.class);
                startActivity(back);
            }
        });


    }


//    public void uploadDialog(){
//        dialogBuilder = new AlertDialog.Builder(this);
//        final View uploadPopupView = getLayoutInflater().inflate(R.layout.popout_upload, null);
//
//        Button uploadFile, uploadText, uploadImage;
//
//        uploadFile = (Button) uploadPopupView.findViewById(R.id.btnFile);
//        uploadText = (Button) uploadPopupView.findViewById(R.id.btnText);
//        uploadImage = (Button) uploadPopupView.findViewById(R.id.btnImage);
//
//        dialogBuilder.setView(uploadPopupView);
//        dialog = dialogBuilder.create();
//        dialog.show();
//
//        Button uploadbackbtn;
//        uploadbackbtn = (Button) uploadPopupView.findViewById(R.id.upload_backbtn);
//
//        uploadbackbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //define cancel button here
//                dialog.dismiss();
//            }
//        });


}