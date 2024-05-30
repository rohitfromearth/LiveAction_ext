package com.ext.liveaction_ext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.ext.liveaction_ext.Screens.Dashboard;
import com.ext.liveaction_ext.Screens.MainActivity;
import com.ext.liveaction_ext.service_clas.Conn_service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Splashsscreen extends AppCompatActivity {

    private static final String TAG = "Splashsscreen";
    private final Conn_service conn = new Conn_service();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Show the splash screen using the SplashScreen API
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashsscreen);

        SharedPreferences sh = getApplicationContext().getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
        new Handler().postDelayed(() -> {
            SharedPreferences.Editor myEdit = sh.edit();
            String dir = getObbDir().getPath();
            ArrayList pack = conn.App_list(getApplicationContext());
            Log.e(TAG, "Failed to get token: " );
            myEdit.putString("dir", dir);
            Set<String> set = new HashSet<String>();
            set.addAll(pack);
            myEdit.putStringSet("APP_LIST", set);

            myEdit.apply();
            Onboarding();
        },2000);

    }

    private void Onboarding() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.e(TAG, "Failed to get token: " );
        // Ensure the splash screen remains visible until the Firebase token is fetched
        if (user != null) {
            user.getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            navigateTo(Dashboard.class);
                        } else {
                            Log.e(TAG, "Failed to get token: " + task.getException());
                            navigateTo((Class<? extends Activity>) MainActivity.class);
                        }

                    });
        } else {
            navigateTo((Class<? extends Activity>) MainActivity.class);
        }


    }



    private void navigateTo(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        finish();
    }
}
