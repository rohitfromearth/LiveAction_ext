package com.ext.liveaction_ext;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPVerificationHandler {
    private FirebaseAuth mAuth;

    public OTPVerificationHandler() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void verifyOTP(String verificationId, String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {


                        // Verification successful, handle the logged in user
                        // You can implement your logic here
                    } else {
                        // Verification failed
                        // You can implement error handling here
                    }
                });
    }
}
