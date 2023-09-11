package com.ext.liveaction_ext;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Pack_api pc = new Pack_api();
    LocationManager locationManager;
    String latitude, longitude, lati, longit;
    CardView Viewbtn;
    String endpot;
    CheckBox termsCheckbox, privacyCheckbox;
    TextView messageTextView, privacyText, termsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        boolean isLocationEnabled = isLocationServicesEnabled(getApplicationContext());

        getSupportActionBar().setTitle("Life Actions");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},4);
//        SharedPreferences sh = getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
//        endpot= sh.getString("endpt","");
//
//        ArrayList pack =  pc.pack_rule(endpot);
//        Set<String> set = new HashSet<String>();
//        set.addAll(pack);



        if(!isLocationEnabled){
            showSettingsAlert();
        }

        messageTextView = findViewById(R.id.validationmasg);
        privacyText = findViewById(R.id.privacyId);
        termsText = findViewById(R.id.termsId);

        termsCheckbox = findViewById(R.id.checkBox); // Replace R.id.checkbox_terms with your actual checkbox ID
        privacyCheckbox = findViewById(R.id.checkBox2); // Replace R.id.checkbox_privacy with your actual checkbox ID
        messageTextView = findViewById(R.id.validationmasg); // Replace R.id.message_textview with your actual TextView ID


        //privacyText.setMovementMethod(LinkMovementMethod.getInstance());
        privacyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("hiiiiiii","helooooo");
                startActivity(new Intent(getApplicationContext(), PrivacyActivity.class));
            }
        });
        termsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("hiiiiiii","helooooo");
                startActivity(new Intent(getApplicationContext(), TermsActivity.class));
            }
        });


        Viewbtn = findViewById(R.id.disclaimerCardView);
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


                    startActivity(new Intent(MainActivity.this, Login_verifcation.class));
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
    public static boolean isLocationServicesEnabled(Context context) {
        int locationMode;
        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MainActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        MainActivity.this.startActivity(intent);
/*
                        int valwhich = which;
                        userChose(String.valueOf(valwhich));
                        Log.e("positive","positive=="+which);*/
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        /*String valwhich = String.valueOf(which);
                        userChose(valwhich);
                        Log.e("positive","positive=="+which);*/
                    }
                });
        alertDialog.show();
    }




}

