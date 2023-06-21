package com.example.liveaction_ext;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Chart_page extends AppCompatActivity {
    private TabLayout tabLayout;
    Pack_api pc=new Pack_api();
    TextView tvP_usernme;
    CardView manag_btn, achieve_btn,faq_btn,connect_btn;

    String id, username;
    int uid_z;
    int uid=0;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                4);
        FirebaseApp.initializeApp(this);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
          uid = sh.getInt("UID", uid_z);
        Log.e("uesrid", String.valueOf(uid));
        username = sh.getString("username","");
        String endpot = "https://lifeactions.online";
//        String endpot= "https://ec4c-2401-4900-5502-32c3-f825-5b5f-e4c9-de4b.ngrok-free.app";
        String dir= getObbDir().getPath();
        ArrayList pack =  pc.pack_rule(endpot);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("endpt", endpot);
        myEdit.putString("dir",dir);
        Set<String> set = new HashSet<String>();
        set.addAll(pack);
        myEdit.putStringSet("APP_LIST", set);
        myEdit.apply();
        if (uid==0){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Optional: Finish the DashboardActivity so that it's not accessible from the back stack
            return;
        }
        else{
            setContentView(R.layout.activity_chart_page);
        }

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        manag_btn=findViewById(R.id.mange_btn);
        achieve_btn=findViewById(R.id.achie_btn);
      connect_btn=findViewById(R.id.connectbtn);
        faq_btn=findViewById(R.id.FAQ_btn);

        manag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Chart_page.this,Manage_screen.class));
            }
        });
      achieve_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Chart_page.this,Achieve.class));
            }
        });
       faq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Chart_page.this,FaQ.class));
            }
        });
        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Chart_page.this,ContactUs.class));
            }
        });
tvP_usernme=findViewById(R.id.usrname);

tvP_usernme.setText(username);

        // Create and set the adapter for the ViewPager
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Link the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    public void onBackPressed() {
        // Disable going back to the previous activity (login page)
        // You can show a toast message or perform any other desired action here

        // Do nothing or show a message
//        Toast.makeText(this, "Back button is disabled", Toast.LENGTH_SHORT).show();

        // Uncomment the line below to exit the app instead of showing a message
        // super.onBackPressed();
    }
}