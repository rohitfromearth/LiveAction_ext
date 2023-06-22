package com.example.liveaction_ext;

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
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Login_verifcation extends AppCompatActivity {
    CardView btn_verify, perm,usaegstat,Accessibilty,sendotp;
    String phone;
    ImageButton close;
    Calendar c;
    String end="";
    private static  final int REQUEST_LOCATION=1;
    Date currentTime;
    LocationManager locationManager;
    String latitude,longitude, lati,longit;
    EditText edtPhone, edtOTP;
    private String verificationId;
    Conn_service servic= new Conn_service();
    private FirebaseAuth mAuth;

    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_verifcation);
        FirebaseApp.initializeApp(this);
        btn_verify= findViewById(R.id.verifybtn);
        sendotp=findViewById(R.id.sendotp);
//perm=findViewById(R.id.Permissions);
        edtOTP=findViewById(R.id.editTextOtp);
        edtPhone=findViewById(R.id.editTextPhone);


        dialog = new Dialog(Login_verifcation.this);
        dialog.setContentView(R.layout.dialog_for_perrmisson);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        end = sh.getString("endpt","");

        mAuth = FirebaseAuth.getInstance();
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String locat= locationfinder();
        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        v.animate().alpha(1f).setDuration(200);
                    }
                }).start();
                if( isValidMobileNumber(edtPhone.getText().toString())){
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
                        Log.e("new_string", "onClick: " + edtPhone.getText().toString());
                        phone = "+91" + edtPhone.getText().toString();
                        Log.e("new_string", "onClick: "+phone);
                        sendVerificationCode(phone);
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("mobile_no", phone);

                        myEdit.apply();
                    }
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login_verifcation.this);

                    // Set the title and message for the dialog
                    builder.setTitle("Validation Error");
                    builder.setMessage("Enter valid mobile number");

                    // Set a positive button and its click listener
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Handle the click event (if needed)
                        }
                    });

                    // Create and show the alert dialog
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }}
        });
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                    verifyCode(edtOTP.getText().toString());

                }
            }
        });


        usaegstat=dialog.findViewById(R.id.usaegstat_perm);
        Accessibilty=dialog.findViewById(R.id.accessibilty_perm);
        close =dialog.findViewById(R.id.close_permission);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().alpha(1f).setDuration(200);
                    }
                }).start();
                dialog.dismiss();
            }
        });
        usaegstat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().alpha(1f).setDuration(200);
                    }
                }).start();
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

                if (usaegstat.isEnabled()) {
                    Accessibilty.setEnabled(true);  // Enable button2
                } else {
                    Accessibilty.setEnabled(false); // Disable button2
                }
            }
        });
        Accessibilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().alpha(1f).setDuration(200);
                    }
                }).start();
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
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

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
//                            Intent i = new Intent(MainActivity.this, Homepage.class);
//                            startActivity(i);


                            FirebaseUser currentUser = mAuth.getCurrentUser();
//                            senddata(edtPhone.getText().toString());

                            if (currentUser != null) {
                                currentUser.getIdToken(true)
                                        .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<GetTokenResult> task) {
                                                if (task.isSuccessful()) {
                                                    String firebaseToken = task.getResult().getToken();

                                                    boolean pageID= senddata(edtPhone.getText().toString(),firebaseToken);
                                                    boolean hasPermission = hasUsageAccessPermission();
                                                    if(hasPermission){
                                                        if (pageID) {
                                                            Log.e("mauth", String.valueOf(mAuth));
                                                            startActivity(new Intent(Login_verifcation.this, Chart_page.class));
                                                        }
                                                        else{
                                                            startActivity(new Intent(Login_verifcation.this,Formpage.class ));
                                                        }
                                                    }
                                                    else{

                                                        btn_verify.setCardBackgroundColor(Color.parseColor("#808080"));
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Login_verifcation.this);

                                                        // Set the title and message for the dialog
                                                        builder.setTitle("Permission Requirement:");
                                                        builder.setMessage("Give Usage Stat Permission");
                                                        //builder.setMessage("Thank You");


                                                        // Set a positive button and its click listener
                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                // Handle the click event (if needed)
                                                            }
                                                        });

                                                        // Create and show the alert dialog
                                                        AlertDialog alertDialog = builder.create();
                                                        alertDialog.show();
                                                    }


                                                    // Pass the firebaseToken to your backend developer through the API
                                                    // You can make an API call and include the token in the request headers or body
                                                }  // Handle error getting the token


                                            }
                                        });
                            }





                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.

                            c = Calendar.getInstance();
                            int min = c.get(Calendar.MINUTE);
                            int hr = c.get(Calendar.HOUR_OF_DAY);
                            int sec = c.get(Calendar.SECOND);
                            int day =c.get(Calendar.DAY_OF_MONTH);
                            int year=c.get(Calendar.YEAR);
                            int mnth=c.get(Calendar.MONTH)+1;
                            String result_str = "Time:"+day+"/"+mnth+"/"+year+":"+hr+":"+min+":"+sec+"Mobile_number:"+phone+"Error:"+task.getException();
                            Log.e("new_string---sign in",task.getException().toString());
                            Log.e("new_exception",result_str);
                            Toast.makeText(Login_verifcation.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            servic.crashLog(result_str,end);

                        }

                    }
                });
    }

    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        // below method is used when
        // OTP is sent from Firebase

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;

            Log.e("onCodeSent", "in oncodesend method");
            Log.e("tokenForm firebase", String.valueOf(forceResendingToken));
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                edtOTP.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);

            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Log.e("new_string", e.getMessage());
            Toast.makeText(Login_verifcation.this, e.getMessage(), Toast.LENGTH_LONG).show();
            c = Calendar.getInstance();
            int min = c.get(Calendar.MINUTE);
            int hr = c.get(Calendar.HOUR_OF_DAY);
            int sec = c.get(Calendar.SECOND);
            int day =c.get(Calendar.DAY_OF_MONTH);
            int year=c.get(Calendar.YEAR);
            int mnth=c.get(Calendar.MONTH)+1;
            String result_str = "Time:"+day+"/"+mnth+"/"+year+":"+hr+":"+min+":"+sec+"Mobile_number:"+phone+"Error:"+e.getMessage();
            Log.e("new_exception",result_str);
            servic.crashLog(result_str,end);
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);

    }
    public boolean senddata(String mob_no,String firbastkn){

        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("mobile_no", mob_no);
            String uiddd= servic.formdatasend(requestBody, "/user/login");


            JSONObject jsonResponse = new JSONObject(uiddd);
            JSONObject data = jsonResponse.getJSONObject("data");
            boolean userExists = data.getBoolean("user_exists");
            if (userExists){
                int userId = data.getInt("user_id");
                Log.e("datastream", String.valueOf(userId));
                String userName = data.getString("user_name");
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("firebaseToken",firbastkn);
                Log.e("datastream", String.valueOf(firbastkn));
                myEdit.putInt("UID",userId);

                myEdit.putString("mobile_number",mob_no);
                myEdit.putString("username",userName);
                myEdit.apply();
                return true;
            }
            else{
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("firebaseToken",firbastkn);

                myEdit.putString("mobile_number",mob_no);
                myEdit.apply();
                return false;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
    public String locationfinder()

    {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(   this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Login_verifcation.this,

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