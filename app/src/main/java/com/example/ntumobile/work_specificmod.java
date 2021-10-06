package com.example.ntumobile;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class work_specificmod extends AppCompatActivity {

    private Button backButton,notesButton;
    private EditText newfolder_foldername;
    private TextView folderHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_specificmod);

        backButton = (Button) findViewById(R.id.back_modspg);
        folderHeader = (TextView) findViewById(R.id.modName);

        String folderNameString;
        folderNameString = getIntent().getExtras().getString("Value");
        Toast.makeText(this,folderNameString,Toast.LENGTH_SHORT).show();
        folderHeader.setText(folderNameString);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notesButton = (Button) findViewById(R.id.btnNotes);
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(work_specificmod.this, work_specificmod_notes.class);
                startActivity(back);
            }
        });
    }

}