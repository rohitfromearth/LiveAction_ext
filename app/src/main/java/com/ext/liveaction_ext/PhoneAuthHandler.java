package com.ext.liveaction_ext;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthHandler {
    private FirebaseAuth mAuth;

    public PhoneAuthHandler() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void sendVerificationCode(Activity Acti, String phoneNumber, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks) {




        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(Acti)                 // Activity (for callback binding)
                        .setCallbacks(callbacks)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
