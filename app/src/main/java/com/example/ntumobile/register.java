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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {

    private TextInputLayout inputEmail, inputPassword,inputConfirm;
    ImageButton register;
    Animation topAnim;
    ImageView image1;
    ProgressDialog mLoadingBar;
    private FirebaseDatabase mDatabase;



    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation_login);
        image1 = (ImageView) findViewById(R.id.imageReg2);
        image1.setAnimation(topAnim);

        inputEmail = findViewById(R.id.inputEmail_Register);
        inputPassword = findViewById(R.id.inputPassword_Register);
        inputConfirm = findViewById(R.id.inputConfirmPassword);

        mLoadingBar = new ProgressDialog(this);

        register = (ImageButton) findViewById(R.id.register_btn);

        mDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=inputEmail.getEditText().getText().toString();
                String password = inputPassword.getEditText().getText().toString();
                String confirmPassword = inputConfirm.getEditText().getText().toString();

                if (email.isEmpty() || !email.contains("@e.ntu.edu.sg")){
                    showError(inputEmail, "Not valid Email");
                }else if(password.isEmpty() || password.length()<5){
                    showError(inputPassword, "Password must be greater than 5 letter");
                }else if(!confirmPassword.equals(password)){
                    showError(inputConfirm, "Password did not match!");
                }

                else{

                    mLoadingBar.setTitle("Registration");
                    mLoadingBar.setMessage("Checking...");
                    mLoadingBar.setCanceledOnTouchOutside(false);
                    mLoadingBar.show();

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                mLoadingBar.dismiss();
                                Toast.makeText(register.this, "Registration is successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(register.this,register_info.class);
                                intent.putExtra("email",email);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {

                                mLoadingBar.dismiss();
                                Toast.makeText(register.this, "Registration fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }







            }
        });


    }



    private void showError(TextInputLayout field, String text) {
        field.setError(text);
        field.requestFocus();
    }



}
