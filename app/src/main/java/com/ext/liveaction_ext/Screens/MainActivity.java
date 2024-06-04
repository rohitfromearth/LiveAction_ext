package com.ext.liveaction_ext.Screens;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.ext.liveaction_ext.R;
import com.ext.liveaction_ext.service_clas.Conn_service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {





    CardView Viewbtn;



    CheckBox termsCheckbox, privacyCheckbox;
    TextView messageTextView, privacyText, termsText;
    //SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        boolean isLocationEnabled = isLocationServicesEnabled(getApplicationContext());

        getSupportActionBar().setTitle(R.string.app_name);
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



        privacyText.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PrivacyActivity.class)));
        termsText.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), TermsActivity.class)));


        Viewbtn = findViewById(R.id.disclaimerCardView);
        Viewbtn.setOnClickListener(v -> {
            v.animate().alpha(0.5f).setDuration(200).withEndAction(() -> v.animate().alpha(1f).setDuration(200)).start();
            boolean termsChecked = termsCheckbox.isChecked();
            boolean privacyChecked = privacyCheckbox.isChecked();

            if (termsChecked && privacyChecked) {

                startActivity(new Intent(MainActivity.this, ProminentActivity.class));
            } else {
                // Display a message below the button
                messageTextView.setText("*Please check both checkboxes.");
            }


        });
        privacyCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Enable or disable the button based on the checkboxes' states
            boolean termsChecked = termsCheckbox.isChecked();
            boolean privacyChecked = privacyCheckbox.isChecked();

            messageTextView.setText("");
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
                (dialog, which) -> {
                    Intent intent = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MainActivity.this.startActivity(intent);
                });
        alertDialog.setNegativeButton("Cancel",
                (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("onBack","main back");
    }

}

