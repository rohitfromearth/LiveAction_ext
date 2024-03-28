package com.ext.liveaction_ext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splashsscreen extends AppCompatActivity {
    private boolean keep = true;
    private final int DELAY = 1250;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        // Install the splash screen and set keep-on-screen condition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> true);
//        splashScreen.setKeepOnScreenCondition(() -> keep);
//        Handler handler = new Handler();
//        handler.postDelayed(() -> keep = false, DELAY);;
        // Add your other initialization logic here.
        // For example, navigate to the main activity after the splash screen.
        Intent intent = new Intent(this, Login_verifcation.class);
        startActivity(intent);
        finish();
    }
}