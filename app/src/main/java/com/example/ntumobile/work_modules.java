package com.example.ntumobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.util.ArrayList;

public class work_modules extends AppCompatActivity {


    private Button mod_addmod, mod_mod1, mod_mod2, mod_mod3, mod_mod4, backButton;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText newfolder_foldername;
    private Button newfolder_cancel, newfolder_add;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_modules);

        mod_addmod = (Button) findViewById(R.id.addMod);
        mod_mod1 = (Button) findViewById(R.id.mod1);
        mod_mod2 = (Button) findViewById(R.id.mod2);
        mod_mod3 = (Button) findViewById(R.id.mod3);
        mod_mod4 = (Button) findViewById(R.id.mod4);


        mod_addmod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFolderDialog();
            }
        });

        mod_mod3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSpecificMod();
            }
        });

        backButton = (Button) findViewById(R.id.back_workpg);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(work_modules.this, work_main.class);
                startActivity(back);
            }
        });


    }

    public void openSpecificMod(){
        Intent intent = new Intent(this, work_specificmod.class);
        //write folder header for the nxt page
        String folderNameString;
        folderNameString = newfolder_foldername.getText().toString();
        intent.putExtra("Value", folderNameString);
        startActivity(intent);
    }

    public void createNewFolderDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View folderPopupView = getLayoutInflater().inflate(R.layout.popout_createnewfolder, null);

        newfolder_foldername = (EditText) folderPopupView.findViewById(R.id.newfolder_foldername);
        newfolder_add = (Button) folderPopupView.findViewById(R.id.newfolder_addButton);
        newfolder_cancel = (Button) folderPopupView.findViewById(R.id.newfolder_cancelButton);

        dialogBuilder.setView(folderPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        //Button Array
        ArrayList<Button> folderButtonList = new ArrayList<>();
        folderButtonList.add(mod_mod1);
        folderButtonList.add(mod_mod2);
        folderButtonList.add(mod_mod3);
        folderButtonList.add(mod_mod4);

        newfolder_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define save button here
                String folderName = newfolder_foldername.getText().toString();
                for (int i= 0; i<4; i++) {
                    if (folderButtonList.get(i).getVisibility() == View.VISIBLE) {
                        // Its visible
                        i =i++;
                    } else {
                        folderButtonList.get(i).setVisibility(View.VISIBLE);
                        folderButtonList.get(i).setText(folderName);

                        break;
                    }
                }
                dialog.dismiss();
            }
        });

        newfolder_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //define cancel button here
                dialog.dismiss();
            }
        });



    }
}