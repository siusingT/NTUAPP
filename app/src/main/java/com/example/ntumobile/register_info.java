package com.example.ntumobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class register_info extends AppCompatActivity {


    private TextInputLayout inputName, inputSchool,inputCourse;
    ImageButton next;
    Animation topAnim;
    ImageView image2;
    ProgressDialog mLoadingBar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);


        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation_login);
        image2 = (ImageView) findViewById(R.id.imageReg2);
        image2.setAnimation(topAnim);

        ImageButton next = (ImageButton)findViewById(R.id.next_button);

        mLoadingBar = new ProgressDialog(this);

        inputName = findViewById(R.id.inputName);
        inputSchool = findViewById(R.id.inputSchool);
        inputCourse = findViewById(R.id.inputCourse);

        database = FirebaseDatabase.getInstance("https://ntu-mobile-9eb73-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mRef = database.getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();

        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    // User is signed in
                    toastMessage("Successfully signed in with: " + user.getEmail());
                }
                else{
                    toastMessage("Not logged in");
                }
            }
        };


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                String userID = user.getUid();

                String name=inputName.getEditText().getText().toString();
                String school = inputSchool.getEditText().getText().toString();
                String course = inputCourse.getEditText().getText().toString();

                HashMap<String, String> userMap = new HashMap<>();

                userMap.put("name", name);
                userMap.put("school", school);
                userMap.put("course", course);
                userMap.put("email", email);
                userMap.put("uid", userID);

                HashMap<Object, String> hashMap = new HashMap<>();
                hashMap.put("onlineStatus", "online");

                mRef.child(userID).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(register_info.this, Home.class);
                            intent.putExtra("name",name);
                            startActivity(intent);

                            Toast.makeText(register_info.this, "Your information is saved successfully!", Toast.LENGTH_SHORT).show();

                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(register_info.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                //mAuth.signInWithEmailAndPassword(email,password);
                Intent intent = new Intent(register_info.this, Home.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}