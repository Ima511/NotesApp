package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpLoginActivity extends AppCompatActivity {

    EditText phoneNumber, otp;
    Button sendOTP , verifyOTP ;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    String verificationID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otplogin);

        phoneNumber = (EditText) findViewById(R.id.editText_phoneNumber);
        otp = (EditText) findViewById(R.id.editText_OTP);
        sendOTP = (Button) findViewById(R.id.button_sendOtp);
        verifyOTP = (Button) findViewById(R.id.button_verifyOtp);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        progressDialog.setMessage("Verifying Phone Number...");


        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(phoneNumber.getText().toString())){
                    Toast.makeText(OtpLoginActivity.this, "Please enter valid phone No.", Toast.LENGTH_SHORT).show();
                }else{
                    String number = phoneNumber.getText().toString();
                    sendVerificationCode(number);

                }

            }
        });

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(otp.getText().toString())){
                    Toast.makeText(OtpLoginActivity.this, " Wrong OTP Entered", Toast.LENGTH_SHORT).show();
                }
                else{
                    verifycode(otp.getText().toString());

                }

            }
        });



    }

    private void verifycode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code);
        signinByCredentials(credential);

    }

    private void signinByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(OtpLoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OtpLoginActivity.this, MainActivity.class));
                }else {

                }

            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber( "+91" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted( @NonNull PhoneAuthCredential credential) {
              final String code = credential.getSmsCode();

              if (code != null ){
                  verifycode(code);
              }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(OtpLoginActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s,token);
            verificationID  = s;
            Toast.makeText(OtpLoginActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
            verifyOTP.setEnabled(true);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null){
            startActivity(new Intent(OtpLoginActivity.this, MainActivity.class));
            finish();
        }
    }
}