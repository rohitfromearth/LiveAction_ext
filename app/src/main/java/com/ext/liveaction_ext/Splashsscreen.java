package com.ext.liveaction_ext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.splashscreen.SplashScreen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.ext.liveaction_ext.Screens.Dashboard;
import com.ext.liveaction_ext.Screens.MainActivity;
import com.ext.liveaction_ext.service_clas.Conn_service;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Splashsscreen extends AppCompatActivity {
    private boolean keep = true;
    private final int DELAY = 1250;

//    ImageView load_img;
String Dir= "";
    int uid_z;
    String TAG = String.valueOf(getClass());
    Conn_service conn = new Conn_service();
    String mobile, username;
    int uid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.AppTheme);

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> true);
        Dir = getObbDir().getPath();
//load_img=findViewById(R.id.Loading_image);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                4);
       // FirebaseApp.initializeApp(this);

        SharedPreferences sh = getApplicationContext().getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
        uid = sh.getInt("UID", uid_z);
        username = sh.getString("username", "");
        mobile = sh.getString("mobile_number", "");





        ArrayList pack = conn.App_list(getApplicationContext());

        SharedPreferences.Editor myEdit = sh.edit();
boolean act=false;
//myEdit.putBoolean("Dashboard",false);
        myEdit.putString("dir", Dir);

        Set<String> set = new HashSet<String>();
        set.addAll(pack);
        myEdit.putStringSet("APP_LIST", set);
        myEdit.apply();


        if (uid == 0) {

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {


            Intent intent = new Intent(this, Dashboard.class);
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