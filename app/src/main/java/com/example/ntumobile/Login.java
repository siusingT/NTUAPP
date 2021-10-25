package com.example.ntumobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private TextInputLayout inputEmail, inputPassword;
    Button btnLogin,btnRegister;
    Animation topAnim;
    ImageView image1, image2;
    FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation_login);
        image1= (ImageView)findViewById(R.id.login_image1);
        image2= (ImageView)findViewById(R.id.login_image2);
        image1.setAnimation(topAnim);
        image2.setAnimation(topAnim);


        //Login
        inputEmail = findViewById(R.id.inputEmail_Register);
        inputPassword = findViewById(R.id.inputPassword_Register);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(this);
        btnLogin = findViewById(R.id.login);


        Button btnRegister = (Button)findViewById(R.id.register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(Login.this, register.class);
                startActivity(register);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AttemptLogin();
            }
        });
    }



    private void AttemptLogin() {
        String email=inputEmail.getEditText().getText().toString();
        String password=inputPassword.getEditText().getText().toString();

        if (email.isEmpty() || !email.contains("@e.ntu.edu.sg")){
            showError(inputEmail, "Not valid Email");
        }else if(password.isEmpty() || password.length()<5){
            showError(inputPassword, "Password must be greater than 5 letter");
        }
        else{
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("Checking...");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(Login.this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        mLoadingBar.dismiss();
                        Toast.makeText(Login.this, "Login is successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this,Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                    else
                    {
                        mLoadingBar.dismiss();
                        Toast.makeText(Login.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }


    private void showError(TextInputLayout field, String text) {
        field.setError(text);
        field.requestFocus();
    }





}