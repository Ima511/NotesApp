package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapp.DATA.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    TextView signUpTitle;
    EditText nameET, emailET, passwordET;
    Button registerButton;
    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;
    DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpTitle = (TextView) findViewById(R.id.text_SignUp_title);
        nameET = (EditText) findViewById(R.id.editText_signup_name);
        emailET = (EditText) findViewById(R.id.editText_signup_email);
        passwordET = (EditText) findViewById(R.id.editText_signup_Password);
        registerButton = (Button) findViewById(R.id.button_Register);
        firebaseAuth =  FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        databaseUser = FirebaseDatabase.getInstance().getReference("USERS TABLE");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();

                if (!name.equalsIgnoreCase("")){

                    if(!email.equalsIgnoreCase("")){

                        if (!password.equalsIgnoreCase("")){

                            firebaseAuth.createUserWithEmailAndPassword(email, password);

                        }else{
                            Toast.makeText(SignUpActivity.this, "Please enter yout Password", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SignUpActivity.this, "Please enter your Email Id ", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(SignUpActivity.this, "Please fill the name", Toast.LENGTH_SHORT).show();
                }

                registerUser(name,email,password);
            }
        });


    }

    private void registerUser(String name, String email, String password) {

        progressDialog.setMessage("Registration in Progress");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    // Save user information into database
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    String uid = currentUser.getUid();

                    UserInfo userInfo =  new UserInfo(name, email, "");

                    databaseUser.child(uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "User successfully Registered", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }else{
                    Toast.makeText(SignUpActivity.this, "Registration is failed...", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}