package com.ext.liveaction_ext;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

//    Pack_api pc = new Pack_api();
    Conn_service servic = new Conn_service();
//    LocationManager locationManager;
//    String latitude, longitude, lati, longit;
    CardView Viewbtn;
    Pack_api pc = new Pack_api();
    Context context;
//    String endpot;
    CheckBox termsCheckbox, privacyCheckbox;
    TextView messageTextView, privacyText, termsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        boolean isLocationEnabled = isLocationServicesEnabled(getApplicationContext());

        getSupportActionBar().setTitle("Life Actions");
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS}, 4);

        if (!isLocationEnabled) {
            showSettingsAlert();
        }

        messageTextView = findViewById(R.id.validationmasg);
        privacyText = findViewById(R.id.privacyId);
        termsText = findViewById(R.id.termsId);

        termsCheckbox = findViewById(R.id.checkBox); // Replace R.id.checkbox_terms with your actual checkbox ID
        privacyCheckbox = findViewById(R.id.checkBox2); // Replace R.id.checkbox_privacy with your actual checkbox ID
        messageTextView = findViewById(R.id.validationmasg); // Replace R.id.message_textview with your actual TextView ID
       // String endpot = "https://zr0prhz1-8080.inc1.devtunnels.ms"; //"https://zr0prhz1-8080.inc1.devtunnels.ms";//
        String endpot = "https://g503wpzp-8080.inc1.devtunnels.ms";

        //String endpot = "https://h17tl5kg-8080.inc1.devtunnels.ms";
        String dir = getObbDir().getPath();
        ArrayList pack = pc.pack_rule(endpot);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LifeSharedPref", context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("endpt", endpot);
        myEdit.putString("dir", dir);
        Set<String> set = new HashSet<String>();
        set.addAll(pack);
        myEdit.putStringSet("APP_LIST", set);
        myEdit.apply();

        privacyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("hiiiiiii", "helooooo");
                startActivity(new Intent(getApplicationContext(), PrivacyActivity.class));
            }
        });
        termsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("hiiiiiii", "helooooo");
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

//                    try {
////                        StoreData();
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
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
//    private JSONArray OttData() throws JSONException {
//        JSONArray arry_ott = new JSONArray();
//
//        JSONObject app1 = new JSONObject();
//        app1.put("app_name", "Netflix");
//        app1.put("installed",true);
//        app1.put("watched_in_last_year",true);
//
//
//
//        arry_ott.put(app1);
//
//
//        return arry_ott;
//    }
//
//    private JSONArray MusictData() throws JSONException {
//        JSONArray arry_music = new JSONArray();
//
//        JSONObject app1 = new JSONObject();
//        app1.put("app_name", "Jio savan");
//        app1.put("installed", true);
//        app1.put("watched_in_last_year",true);
//
//
//
//        arry_music.put(app1);
//
//
//
//        return arry_music;
//    }
//    public void StoreData() throws JSONException {
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        JSONArray jsonAry = new JSONArray();
//        jsonAry.put("value1");
//        jsonAry.put("value2");
//        jsonAry.put("value3");
//        JSONArray ottdta = OttData();
//        JSONArray musicdata = MusictData();

//        JSONObject jsonBody = new JSONObject();
//        jsonBody.put("mobile_no", "8483858864");
//        jsonBody.put("name", "Dummyname");
//        jsonBody.put("birthDate", 21);
//        jsonBody.put("birthMonth", 1);
//        jsonBody.put("birthYear",2001);
//
//
//            jsonBody.put("city","East Godavari");
//
//            jsonBody.put("state", "Andhra Pradesh");
//
//
//        jsonBody.put("gender", "male");
//        jsonBody.put("emailId", "dummy@dummy.com");
//        jsonBody.put("education", "Up to standard 4" );
//        jsonBody.put("occupation", "Working full time (8 hours a day)");
//
//        jsonBody.put("durablesUsed","Two-Wheeler, Washing Machine");
//        jsonBody.put("shoppedOnline", true);
//        jsonBody.put("productsPurchasedOnline","Clothing & Footwear, Beauty & personal Care (soaps, shampoo, skin creams, makeup etc)");
//        jsonBody.put("lastOnlineWatch", "Yesterday");
//        jsonBody.put("ottWatchLastYear", ottdta);
//        jsonBody.put("musicAppsLastYear", musicdata);
//        jsonBody.put("feedbackChoice","Yes, via call or via Sms");
//        jsonBody.put("latitude", "");
//        jsonBody.put("longitude","");
//
//        String userId = servic.formdatasend(jsonBody, "/user/create");
//        try {
//            JSONObject jsonResponse = new JSONObject(userId);
//            int uId = jsonResponse.getInt("user_id");
//Log.e("testrun", String.valueOf(uId));
//
//
//
//        } catch (JSONException e) {
//     Log.e("testrun2", e.getMessage());
//        }
//
//    }

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
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
        alertDialog.show();
    }


}

