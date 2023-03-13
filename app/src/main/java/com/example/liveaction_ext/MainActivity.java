package com.example.liveaction_ext;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button Sbmt_btn,Access_btn;
    Spinner Education,Ocupation ;
    EditText et_abt;

    StringBuilder result;
    Profile_data_stream pds;
    Pack_api pa;
    CheckBox tw_wlr,refrgtr,pc,Ac,colrtv,wshingmchine,car,agri;
    private FirebaseAuth mAuth;
    Dialog dialog1;
    Dialog dialog;
    // variable for our text input
    // field for phone and OTP.
    private EditText edtPhone, edtOTP;
    String mob_no="";
Button close;
    // buttons for generating OTP and verifying OTP
    private Button verifyOTPBtn, generateOTPBtn;

    // string for storing our verification ID
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                4);

 dialog1 = new Dialog(MainActivity.this);
 dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.data);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        dialog1.setContentView(R.layout.otp_regestration);

//        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog1.setCancelable(false);
//        dialog1.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

// Set the window animations
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();

        // initializing variables for button and Edittext.
        edtPhone = dialog1.findViewById(R.id.idEdtPhoneNumber);
        edtOTP = dialog1.findViewById(R.id.idEdtOtp);
        verifyOTPBtn = dialog1.findViewById(R.id.idBtnVerify);
        generateOTPBtn = dialog1.findViewById(R.id.idBtnGetOtp);

        Access_btn = dialog.findViewById(R.id.accessicibilty);
        Sbmt_btn= dialog.findViewById(R.id.submit);
        Education =dialog.findViewById(R.id.Edu_spn);
        Ocupation = dialog.findViewById(R.id.ocup_spn);
        tw_wlr=dialog.findViewById(R.id.ckbx_twler);
        refrgtr=dialog.findViewById(R.id.ckbx_refrigtr);
        wshingmchine=dialog.findViewById(R.id.ckbx_wshingmchin);
        car=dialog.findViewById(R.id.ckbx_car);
        colrtv=dialog.findViewById(R.id.ckbx_clrtv);
        agri=dialog.findViewById(R.id.ckbx_agri);
        pc=dialog.findViewById(R.id.ckbx_pc);
        Ac=dialog.findViewById(R.id.ckbx_ac);
        et_abt = dialog.findViewById(R.id.et_about);
        ///////

close=dialog1.findViewById(R.id.close);

close.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        dialog1.dismiss();
        dialog.show();
    }
});
        generateOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // below line is for checking whether the user
                // has entered his mobile number or not.
                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    // when mobile number text field is empty
                    // displaying a toast message.
                    Log.e("new_string", "Mobile number field is empty");
                    Toast.makeText(MainActivity.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
                } else {
                    // if the text field is not empty we are calling our
                    // send OTP method for getting OTP from Firebase.
                    Log.e("new_string", "onClick: "+edtPhone.getText().toString());
                    String phone = "+91" + edtPhone.getText().toString();
                    sendVerificationCode(phone);
                }
            }
        });

        // initializing on click listener
        // for verify otp button
        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validating if the OTP text field is empty or not.
                if (TextUtils.isEmpty(edtOTP.getText().toString())) {
                    // if the OTP text field is empty display
                    // a message to user to enter OTP
                    Toast.makeText(MainActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    // if OTP field is not empty calling
                    // method to verify the OTP.

                    verifyCode(edtOTP.getText().toString());

                }
            }
        });





        //////
        ArrayAdapter<CharSequence> edu_adap=ArrayAdapter.createFromResource(this, R.array.Education, android.R.layout.simple_spinner_item);

        edu_adap.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Education.setAdapter(edu_adap);
        ArrayAdapter<CharSequence>occu_adap=ArrayAdapter.createFromResource(this, R.array.Ocupation, android.R.layout.simple_spinner_item);

        occu_adap.setDropDownViewResource(android.R.layout.simple_spinner_item);
        Ocupation.setAdapter(occu_adap);
        result=new StringBuilder();

        if(agri.isChecked()){
            result.append("Agricultural,");
        }
        if(car.isChecked()){
            result.append("Car / Jeep/ Van,");
        }
        if(tw_wlr.isChecked()){
            result.append("two Wheeler,");
        }
        if(Ac.isChecked()){
            result.append("Air Conditioner,");
        }
        if(wshingmchine.isChecked()){
            result.append("Washing machine,");
        }
        if(colrtv.isChecked()){
            result.append("Color tv,");
        }
        if(refrgtr.isChecked()){
            result.append("Refrigerator,");
        }
        if(pc.isChecked()){
            result.append("Personal Computer / Laptop ,");
        }
pa = new Pack_api();

pds= new Profile_data_stream();


        Access_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

            }
        });

        Sbmt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                set.addAll(arrPackage);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);
                String education= (String)Education.getSelectedItem();
                String occupation= (String)Ocupation.getSelectedItem();
                String chekbox_choices=result.toString();
                String About = et_abt.getText().toString().trim();

                String U_abt= About;
                String U_edu=  education;
                String U_ocu=  occupation;
                String U_Chek =  chekbox_choices;
                String U_data = About+education+occupation+chekbox_choices;
                String urlString = "https://lifeactions.online/user/create";

//                String urlString = "https://perfect-eel-fashion.cyclic.app/user/create"; // URL to call dont touch
//                String urlString = "https://9f88-122-169-92-160.in.ngrok.io/user/create";//temp_url
//                String urlString = About+"/user/create";

                try {
                    SharedPreferences share = getSharedPreferences("MySharedPref", MODE_PRIVATE);

     mob_no= share.getString("Mob_no", "");

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("about", U_abt);
                jsonBody.put("education", U_edu);
                jsonBody.put("occupation", U_ocu);
                jsonBody.put("durables_used", U_Chek);
                jsonBody.put("Mobile_no", mob_no);
                String respons = pds.datasender(urlString,jsonBody);              //calling Profile data stream
                    ArrayList pack=pa.pack_rule();
                    String dir= getObbDir().getPath();
String endpoint= "https://lifeactions.online";
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    Set<String> set = new HashSet<String>();
                    set.addAll(pack);
                    myEdit.putString("dir",dir);
                    myEdit.putStringSet("APP_LIST", set);
                    myEdit.putString("endpt", endpoint);

                    myEdit.putString("Userid", respons);

                    myEdit.apply();
                }
                catch (Exception e) {
                    Log.e("techh", String.valueOf(e));
                    System.out.println("Error in api calling: "+ String.valueOf(e));
                }
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));

            }

            });

        dialog1.show();
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
                          dialog1.dismiss();
                          dialog.show();
                            String mob=edtPhone.getText().toString();
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("Mob_no", mob);

                            myEdit.apply();
//                            finish();

                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Log.e("new_string",task.getException().toString());
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

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
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
}