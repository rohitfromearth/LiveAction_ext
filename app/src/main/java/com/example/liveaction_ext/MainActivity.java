package com.example.liveaction_ext;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static  final int REQUEST_LOCATION=1;

    Pack_api pc=new Pack_api();

    LocationManager locationManager;
    String latitude,longitude, lati,longit;
    CardView Viewbtn;
    String endpot;
    CheckBox termsCheckbox, privacyCheckbox;
    TextView messageTextView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                4);
//        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        endpot= sh.getString("endpt","");
//
//        ArrayList pack =  pc.pack_rule(endpot);
//        Set<String> set = new HashSet<String>();
//        set.addAll(pack);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String locat= locationfinder();
        Log.e("locationdata",locat);
        messageTextView =findViewById(R.id.validationmasg);

        termsCheckbox = findViewById(R.id.checkBox); // Replace R.id.checkbox_terms with your actual checkbox ID
        privacyCheckbox =findViewById(R.id.checkBox2); // Replace R.id.checkbox_privacy with your actual checkbox ID
        messageTextView = findViewById(R.id.validationmasg); // Replace R.id.message_textview with your actual TextView ID


        Viewbtn=findViewById(R.id.disclaimerCardView);


        Viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        v.animate().alpha(1f).setDuration(200);
                    }
                }).start();
                boolean termsChecked = termsCheckbox.isChecked();
                boolean privacyChecked = privacyCheckbox.isChecked();

                if (termsChecked && privacyChecked) {
                    // Perform the button action
                    // Add your logic here
                    // For example, start the Manage_screen activity
                    startActivity(new Intent(MainActivity.this,Login_verifcation.class));
                } else {
                    // Display a message below the button
                    messageTextView.setText("*Please check both checkboxes.");
                }


            }
        });
        privacyCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Enable or disable the button based on the checkboxes' states
                boolean termsChecked = termsCheckbox.isChecked();
                boolean privacyChecked = privacyCheckbox.isChecked();

//                Viewbtn.setEnabled(termsChecked && privacyChecked);

                // Clear the message below the button
                messageTextView.setText("");
            }
        });

    }




    public String locationfinder()

    {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(   this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,

                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

                Log.e("dataloc",latitude+longitude);
                lati = latitude;
                longit = longitude;
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("latitude",longit);
                myEdit.putString("longitude",lati);

                myEdit.apply();


                //showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                Log.e("dataloc2",latitude+longitude);
                lati = latitude;
                longit = longitude;
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("latitude",longit);
                myEdit.putString("longitude",lati);

                myEdit.apply();


                //showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                Log.e("dataloc3",latitude+longitude);
                lati = latitude;
                longit = longitude;
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("latitude",longit);
                myEdit.putString("longitude",lati);

                myEdit.apply();


                // showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            //Thats All Run Your App
        }

        return latitude+longitude;
    }


}

