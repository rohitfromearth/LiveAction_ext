package com.example.liveaction_ext;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.Calendar;

public class Firebasserv extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        // Update the token in the shared preferences or wherever you store it
        updateTokenInSharedPreferences(token);
    }

    private void updateTokenInSharedPreferences(String token) {
        final Calendar c = Calendar.getInstance();
Log.e("Token Updated",c.get(Calendar.MINUTE)+":"+c.get(Calendar.HOUR_OF_DAY)+token );
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("firebaseToken",token);
        myEdit.apply();

        // Update the token in the shared preferences or your preferred storage mechanism
    }
}