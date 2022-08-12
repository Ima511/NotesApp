package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    EditText  emailText, passwordText;
    TextView loginText,signUpText, forgotPasswordText, otpLogin;
    FirebaseAuth firebaseAuth;

    Button login;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailText = (EditText) findViewById(R.id.editText_email);
        passwordText = (EditText) findViewById(R.id.editText_password);
        signUpText = (TextView) findViewById(R.id.text_signUp);
        loginText = (TextView) findViewById(R.id.text_Login);
        login = (Button) findViewById(R.id.butto_login);
        forgotPasswordText = (TextView) findViewById(R.id.text_forgot_password);
        otpLogin = (TextView) findViewById(R.id.text_otp_login);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                
                if (!email.equalsIgnoreCase("")){
                    if(!password.equalsIgnoreCase("")){
                        loginUser(email, password);
                    }else{
                        Toast.makeText(LoginActivity.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Please enter your Email id", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailText.getText().toString();
                if (!email.equalsIgnoreCase("")){

                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Toast.makeText(LoginActivity.this, "Please check your email for resetting password", Toast.LENGTH_SHORT).show();
                       }else{
                           String errorMsg  = task.getException().getMessage();
                           Toast.makeText(LoginActivity.this, "" + errorMsg, Toast.LENGTH_SHORT).show();
                       }
                        }
                    });

                }else{

                    Toast.makeText(LoginActivity.this, "Please enter valid Email Address", Toast.LENGTH_SHORT).show();

                }
            }
        });

        otpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this,OtpLoginActivity.class);
                    startActivity(intent);
                    finish();
            }
        });


    }


    public void loginUser(String email, String password){
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                        String errorMsg =  task.getException().getMessage();
                    Toast.makeText(LoginActivity.this, "" + errorMsg, Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}