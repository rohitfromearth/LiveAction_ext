package com.ext.liveaction_ext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.splashscreen.SplashScreen;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Splashsscreen extends AppCompatActivity {
    private boolean keep = true;
    private final int DELAY = 1250;
    Pack_api pc = new Pack_api();
//    ImageView load_img;

    int uid_z;
    String mobile, username;
    int uid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.AppTheme);

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> true);

//load_img=findViewById(R.id.Loading_image);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                4);
        FirebaseApp.initializeApp(this);

        SharedPreferences sh = getApplicationContext().getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
        uid = sh.getInt("UID", uid_z);
        username = sh.getString("username", "");
        mobile = sh.getString("mobile_number", "");

//        String endpot = "https://h17tl5kg-8080.inc1.devtunnels.ms";
        String endpot = "https://sondersort.online";


        String dir = getObbDir().getPath();
        ArrayList pack = pc.pack_rule(endpot);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("endpt", endpot);
        myEdit.putString("dir", dir);
        Set<String> set = new HashSet<String>();
        set.addAll(pack);
        myEdit.putStringSet("APP_LIST", set);
        myEdit.apply();


        if (uid == 0) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {


            Intent intent = new Intent(this, Chart_page.class);
        startActivity(intent);
        finish();

        }







//        splashScreen.setKeepOnScreenCondition(() -> keep);
//        Handler handler = new Handler();
//        handler.postDelayed(() -> keep = false, DELAY);;
        // Add your other initialization logic here.
        // For example, navigate to the main activity after the splash screen.



//
//        Intent intent = new Intent(this, Login_verifcation.class);
//        startActivity(intent);
//        finish();
    }
}