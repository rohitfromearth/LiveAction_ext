package com.ext.liveaction_ext.Screens;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.ext.liveaction_ext.service_clas.OTPVerificationHandler;
import com.ext.liveaction_ext.service_clas.PhoneAuthHandler;
import com.ext.liveaction_ext.R;
import com.ext.liveaction_ext.service_clas.Conn_service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;

import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Login_verifcation extends AppCompatActivity {

    protected LocationManager locationManager;
    CardView btn_verify, perm, usaegstat, usaegstat1, Accessibilty, Accessibilty1, sendotp, permission_usage;
    String phone;
    ImageButton close, close1;
    Calendar c;

    private static final int REQUEST_LOCATION = 1;
    EditText edtPhone, edtOTP;
    Conn_service servic = new Conn_service();
    Dialog dialog;
    private String verificationId;
    private FirebaseAuth mAuth;
    String latitude = "", longitude = "", city = "";
    String  state = "";
    Context context;
    Location lGps, netPro, pasee;
    boolean lgps_enabled = false;
    boolean netpro_enabled = false;
    boolean passive_enabled = false;
    SharedPreferences sh;
    private FirebaseAuthSettings firebaseAuthSettings;
    private PhoneAuthHandler phoneAuthHandler;
    private OTPVerificationHandler otpVerificationHandler;

    // callback method is called on Phone auth provider.

    boolean isLoggedIn = true;
    boolean isFirstPage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_verifcation);

        //FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                PlayIntegrityAppCheckProviderFactory.getInstance());

        getSupportActionBar().setTitle(R.string.app_name);
        btn_verify = findViewById(R.id.verifybtn);
        sendotp = findViewById(R.id.sendotp);
        permission_usage = findViewById(R.id.permission_button);
        edtOTP = findViewById(R.id.editTextOtp);
        edtPhone = findViewById(R.id.editTextPhone);

        btn_verify.setEnabled(false);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String data = getLocation();


        phoneAuthHandler = new PhoneAuthHandler();
        otpVerificationHandler = new OTPVerificationHandler();

        sh = getApplicationContext().getSharedPreferences("LifeSharedPref", MODE_PRIVATE);


        dialog = new Dialog(Login_verifcation.this);
        dialog.setContentView(R.layout.dialog_for_perrmisson);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        mAuth = FirebaseAuth.getInstance();
//        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        firebaseAuthSettings = mAuth.getFirebaseAuthSettings();
//        firebaseAuthSettings.setAppVerificationDisabledForTesting(true);
        usaegstat = dialog.findViewById(R.id.usaegstat_perm);
        Accessibilty = dialog.findViewById(R.id.accessibilty_perm);
        close = dialog.findViewById(R.id.close_permission);


        permission_usage.setOnClickListener(v -> {
            Dialog dialog1 = new Dialog(Login_verifcation.this);
            dialog1.setContentView(R.layout.dialog_for_perrmisson);
            dialog1.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog1.setCancelable(false);
            dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog1.show();
            usaegstat1 = dialog1.findViewById(R.id.usaegstat_perm);
            Accessibilty1 = dialog1.findViewById(R.id.accessibilty_perm);
            close1 = dialog1.findViewById(R.id.close_permission);

            close1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            view.animate().alpha(1f).setDuration(200);
                        }
                    }).start();
                    dialog1.dismiss();
                }
            });
            usaegstat1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            view.animate().alpha(1f).setDuration(200);
                        }
                    }).start();
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

                    // Disable button2
                    Accessibilty1.setEnabled(usaegstat1.isEnabled());  // Enable button2
                }
            });
            Accessibilty1.setOnClickListener(view -> {
                view.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view.animate().alpha(1f).setDuration(200)).start();
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            });
        });
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        v.animate().alpha(1f).setDuration(200);
                    }
                }).start();
                if (isValidMobileNumber(edtPhone.getText().toString())) {
                    Log.e("herewego","damn i am here stuck");
                    // below line is for checking whether the user
                    // has entered his mobile number or not.
                    if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                        // when mobile number text field is empty
                        // displaying a toast message.
                        Log.e("new_string", "Mobile number field is empty");
                        Toast.makeText(Login_verifcation.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                    } else {
                        // if the text field is not empty we are calling our
                        // send OTP method for getting OTP from Firebase.
                      //  Log.e("new_string", "onClick: " + edtPhone.getText().toString());
                        phone = "+91" + edtPhone.getText().toString();

                      //  Log.e("new_string", "onClick: " + phone);
//                        sendVerificationCode(phone);

                        phoneAuthHandler.sendVerificationCode(Login_verifcation.this, phone, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                // Auto-retrieval or instant verification completed, you can also use this credential for signing in

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                // Verification failed
                                Log.e("nonchalant", e.getMessage());
                                Toast.makeText(Login_verifcation.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                // OTP sent successfully, save verification ID
                                verificationId = s;
                                Toast.makeText(Login_verifcation.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                                btn_verify.setEnabled(true);
                            }
                        });

                        SharedPreferences sharedPreferences = getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("mobile_no", phone);

                        myEdit.apply();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            edtOTP.setAutofillHints("one-time-code");
                        }
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login_verifcation.this);

                    // Set the title and message for the dialog
                    builder.setTitle("Validation Error");
                    builder.setMessage("Enter valid mobile number");

                    // Set a positive button and its click listener
                    builder.setPositiveButton("OK", (dialog, which) -> {
                        // Handle the click event (if needed)
                    });

                    // Create and show the alert dialog
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
        btn_verify.setOnClickListener(v -> {

            v.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                @Override
                public void run() {
                    v.animate().alpha(1f).setDuration(200);
                }
            }).start();
            // validating if the OTP text field is empty or not.
            if (TextUtils.isEmpty(edtOTP.getText().toString())) {
                // if the OTP text field is empty display
                // a message to user to enter OTP
                Toast.makeText(Login_verifcation.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
            } else {
                // if OTP field is not empty calling
                // method to verify the OTP.

             verifyOTP(verificationId, edtOTP.getText().toString());

//                    verifyCode(edtOTP.getText().toString());
            }
        });
        close.setOnClickListener(view -> {
            view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                @Override
                public void run() {
                    view.animate().alpha(1f).setDuration(200);
                }
            }).start();
            dialog.dismiss();
        });
        usaegstat.setOnClickListener(view -> {
            view.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view.animate().alpha(1f).setDuration(200)).start();
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

            // Disable button2
            Accessibilty.setEnabled(usaegstat.isEnabled());  // Enable button2
        });
        Accessibilty.setOnClickListener(view -> {
            view.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view.animate().alpha(1f).setDuration(200)).start();
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        });
    }



    public boolean isValidMobileNumber(String number) {
        // Regular expression pattern for mobile number validation
        String regex = "^[1-9]\\d{9}$";

        // Check if the number matches the pattern
        return number.matches(regex);
    }

    private boolean hasUsageAccessPermission() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return mode == AppOpsManager.MODE_ALLOWED;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public boolean senddata(String mob_no, String firbastkn) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("mobile_no", mob_no);
            String uiddd = servic.formdatasend(requestBody, "/user/login");
            JSONObject jsonResponse = new JSONObject(uiddd);
            JSONObject data = jsonResponse.getJSONObject("data");
//            boolean userExists = data.getBoolean("user_exists");
           boolean userExists=false;
            if (userExists) {
                int userId = data.getInt("user_id");
                String userName = data.getString("user_name");
                SharedPreferences.Editor myEdit = sh.edit();
                myEdit.putString("firebaseToken", firbastkn);
                myEdit.putInt("UID", userId);
                myEdit.putString("mobile_number", mob_no);
                myEdit.putString("username", userName);
                myEdit.apply();
                return true;
            } else {

                SharedPreferences.Editor myEdit = sh.edit();
                myEdit.putString("firebaseToken", firbastkn);
                myEdit.putString("mobile_number", mob_no);
                myEdit.apply();
                return false;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    //code start location
    private String getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Login_verifcation.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {

            lgps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            netpro_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            passive_enabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

            lGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            netPro = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            pasee = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (lGps != null) {
                double lat = lGps.getLatitude();
                double longi = lGps.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                Geocoder geocoder = new Geocoder(
                        Login_verifcation.this, Locale
                        .getDefault());

                List<Address> addresses;
                try {

                    addresses = geocoder.getFromLocation(lat,
                            longi, 1);


                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();

                } catch (IOException e) {

                    e.printStackTrace();
                }

            } else if (netPro != null) {
                double lat = netPro.getLatitude();
                double longi = netPro.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                Geocoder geocoder = new Geocoder(
                        Login_verifcation.this, Locale
                        .getDefault());
                List<Address> addresses;
                try {

                    addresses = geocoder.getFromLocation(lat,
                            longi, 1);


                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();

                } catch (IOException e) {

                    e.printStackTrace();
                }
            } else if (pasee != null) {
                double lat = pasee.getLatitude();
                double longi = pasee.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                Geocoder geocoder = new Geocoder(
                        Login_verifcation.this, Locale
                        .getDefault());
                List<Address> addresses;
                try {

                    addresses = geocoder.getFromLocation(lat,
                            longi, 1);


                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();

                } catch (IOException e) {

                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "not finding location.", Toast.LENGTH_SHORT).show();
            }
        }
        return latitude;
    }////////////end method
    //code end location

    public void verifyOTP(String verificationId, String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        signInWithPhoneAuthCredential(credential);


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {


        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // if the code is correct and the task is successful

                        FirebaseUser currentUser = mAuth.getCurrentUser();

                        if (currentUser != null) {
                            currentUser.getIdToken(true)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {

                                            try{
                                                String firebaseToken = task1.getResult().getToken();
                                                       boolean pageID = senddata(edtPhone.getText().toString(), firebaseToken);




                                                String username = sh.getString("username", "");


                                                boolean hasPermission = hasUsageAccessPermission();

                                                if (hasPermission) {
                                                    if (pageID) {


                                                        startActivity(new Intent(Login_verifcation.this, Dashboard.class));
                                                    } else {
                                                        String data = getLocation();


                                                        boolean is_val;
                                                        if (Objects.equals(city, "") && Objects.equals(state, "")) {
                                                            is_val = false;
                                                        } else {
                                                            is_val = true;
                                                        }

                                                        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("LifeSharedPref", context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPref.edit();
                                                        editor.putString("value", city);
                                                        editor.putString("valueState", state);
                                                        editor.putString("longitude", longitude);
                                                        editor.putString("latitude", latitude);
                                                        editor.putBoolean("enabled", is_val);
                                                        editor.apply();
                                                        //Log.d("location",city+" and "+state);
                                                        startActivity(new Intent(Login_verifcation.this, Formpage.class));

                                                    }
                                                } else {

                                                    btn_verify.setCardBackgroundColor(Color.parseColor("#808080"));
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login_verifcation.this);

                                                    // Set the title and message for the dialog
                                                    builder.setTitle("Permission Requirement:");
                                                    builder.setMessage("Give Usage Stat Permission");
                                                    //builder.setMessage("Thank You");


                                                    // Set a positive button and its click listener
                                                    builder.setPositiveButton("OK", (dialog, which) -> {
                                                        // Handle the click event (if needed)
                                                    });

                                                    // Create and show the alert dialog
                                                    AlertDialog alertDialog = builder.create();
                                                    alertDialog.show();
                                                }

                                            } catch (Exception e) {
                                                Log.e("crashtext", e.getMessage());
                                            }

                                            // Pass the firebaseToken to your backend developer through the API
                                            // You can make an API call and include the token in the request headers or body
                                        }  // Handle error getting the token


                                    });
                        }


                    } else {


                        c = Calendar.getInstance();
                        int min = c.get(Calendar.MINUTE);
                        int hr = c.get(Calendar.HOUR_OF_DAY);
                        int sec = c.get(Calendar.SECOND);
                        int day = c.get(Calendar.DAY_OF_MONTH);
                        int year = c.get(Calendar.YEAR);
                        int mnth = c.get(Calendar.MONTH) + 1;
                        String result_str = "Time:" + day + "/" + mnth + "/" + year + ":" + hr + ":" + min + ":" + sec + "Mobile_number:" + phone + "Error:" + task.getException();
                        Log.e("new_string---sign in", task.getException().toString());
                        //  Log.e("new_exception", result_str);
                        Toast.makeText(Login_verifcation.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        servic.crashLog(result_str );

                    }

                });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("onBackPressed","back from login");

        /*if (!isLoggedIn) {
           // super.onBackPressed();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            //finish(); // Finish the current activity
            Log.d("onBack","back to main1");
        } else {
            // User is logged in, perform default back press action
            super.onBackPressed();
            Log.d("onBack","back to main2");
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("destroyed","login destroyed");
    }
}