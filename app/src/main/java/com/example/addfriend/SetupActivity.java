package com.example.addfriend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SetupActivity extends AppCompatActivity {

    private EditText Username, Fullname, School;
    private Button button;
    private String currentUserID;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference UsersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = db.getReference().child("Users").child(currentUserID);

        Username = (EditText) findViewById(R.id.setup_username);
        Fullname = (EditText) findViewById(R.id.setup_fullname);
        School = (EditText) findViewById(R.id.setup_school);
        button = findViewById(R.id.setup_submit);
        loadingBar = new ProgressDialog(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SaveInformation();
            }
        });
    }

    private void SaveInformation()
    {
        String username = Username.getText().toString();
        String fullname = Fullname.getText().toString();
        String school = School.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please write your username...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(fullname)) {
            Toast.makeText(this, "Please write your full name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(school)) {
            Toast.makeText(this, "Please write your school...", Toast.LENGTH_SHORT).show();
        } else
            {
                loadingBar.setTitle("Saving Information");
                loadingBar.setMessage("Please wait, while we are saving your information.");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                HashMap<String, String> userMap = new HashMap<>();

                userMap.put("username", username);
                userMap.put("fullname", fullname);
                userMap.put("school", school);

                UsersRef.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            SendUserToMainActivity();

                            Toast.makeText(SetupActivity.this, "Your information is saved successfully!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(SetupActivity.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });

            }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}