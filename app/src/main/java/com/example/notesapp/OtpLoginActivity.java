package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpLoginActivity extends AppCompatActivity {

    EditText phoneNumber;
    Button verifyButton;
    ProgressDialog progressDialog;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationStateChangedCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otplogin);

        phoneNumber = (EditText) findViewById(R.id.editText_phoneNumber);
        verifyButton = (Button) findViewById(R.id.button_verify);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Verifying Phone Number...");
        verificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted( PhoneAuthCredential phoneAuthCredential) {
                progressDialog.dismiss();
                Toast.makeText(OtpLoginActivity.this, "Mobile number verified successfully.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVerificationFailed( FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(OtpLoginActivity.this, "Verification failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mobileNumber = "+91" + phoneNumber.getText().toString();

                if(!mobileNumber.equalsIgnoreCase("")){
                            verifyMobileNumber(mobileNumber);
                }else{

                    String errorMsg = phoneNumber.getError().toString();
                    Toast.makeText(OtpLoginActivity.this, "" + errorMsg, Toast.LENGTH_LONG).show();

                }

            }
        });



    }


    public void verifyMobileNumber(String mobile){
        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobile, 60L, TimeUnit.SECONDS,this,verificationStateChangedCallbacks);
    }

}