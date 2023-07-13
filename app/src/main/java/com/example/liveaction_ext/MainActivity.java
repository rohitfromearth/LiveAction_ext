package com.example.liveaction_ext;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;

    Pack_api pc = new Pack_api();
    String cityName="";
    String stateName="";

    LocationManager locationManager;
    String latitude, longitude, lati, longit;
    CardView Viewbtn;
    String endpot;
    CheckBox termsCheckbox, privacyCheckbox;
    TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},4);
//        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
//        endpot= sh.getString("endpt","");
//
//        ArrayList pack =  pc.pack_rule(endpot);
//        Set<String> set = new HashSet<String>();
//        set.addAll(pack);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
locationfinder();

        messageTextView = findViewById(R.id.validationmasg);

        termsCheckbox = findViewById(R.id.checkBox); // Replace R.id.checkbox_terms with your actual checkbox ID
        privacyCheckbox = findViewById(R.id.checkBox2); // Replace R.id.checkbox_privacy with your actual checkbox ID
        messageTextView = findViewById(R.id.validationmasg); // Replace R.id.message_textview with your actual TextView ID


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
                    double[] locat= locationfinder();
                    double log1=locat[0];
                    double log2=locat[1];

                cityName = getCityName(getApplicationContext(), log1, log2);
                 stateName = getStateName(getApplicationContext(), log1, log2);
                    boolean is_val;
                    if(Objects.equals(cityName, "") && Objects.equals(stateName, "")){
                        is_val = false;
                    }
                    else{
                        is_val = true;
                    }


                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putBoolean("locat", is_val);
                    myEdit.putString("latitude", longit);
                    myEdit.putString("longitude", lati);


                    myEdit.putString("city", cityName);
                    myEdit.putString("state", stateName);

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


    public double[] locationfinder(){
        double[] result = new double[2];
        if (ActivityCompat.checkSelfPermission(   this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,android.Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED) {
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
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                Log.e("dataloc", latitude + longitude);
                lati = latitude;
                longit = longitude;

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("latitude", longit);
                myEdit.putString("longitude", lati);

                myEdit.apply();
                result[0] = lat;
                result[1] = longi;

                return result;

            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                Log.e("dataloc", latitude + longitude);
                lati = latitude;
                longit = longitude;

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("latitude", longit);
                myEdit.putString("longitude", lati);

                myEdit.apply();
                result[0] = lat;
                result[1] = longi;

                return result;
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                Log.e("dataloc", latitude + longitude);
                lati = latitude;
                longit = longitude;

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("latitude", longit);
                myEdit.putString("longitude", lati);

                myEdit.apply();
                result[0] = lat;
                result[1] = longi;

                return result;
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
                return null;
            }

        }

        return result ;
    }
    public static String getCityName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String cityName = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                cityName = address.getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityName;
    }

    public static String getStateName(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String stateName = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                stateName = address.getAdminArea();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stateName;
    }


}

