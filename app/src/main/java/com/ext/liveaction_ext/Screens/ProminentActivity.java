package com.ext.liveaction_ext.Screens;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ext.liveaction_ext.R;

public class ProminentActivity extends AppCompatActivity {
    CardView btn_sbmt;
    RadioGroup rdgrp;
    boolean goahead= false;
    TextView privacyTextClick, termsTextClick;
    RadioButton radioAgree,radioDisagree;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.prominent_disclosure);
radioAgree=findViewById(R.id.rdagree);
radioDisagree=findViewById(R.id.rddisagree);
        rdgrp= findViewById(R.id.radiogrppDisclosure);
privacyTextClick=findViewById(R.id.privacyIdd);
termsTextClick= findViewById(R.id.termsIdd);
btn_sbmt=findViewById(R.id.btn_submit_Disclosure);

        rdgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdagree) {
                    btn_sbmt.setEnabled(true);
                } else if (checkedId == R.id.rddisagree) {
                    btn_sbmt.setEnabled(false);
                }
            }
        });

        btn_sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioDisagree.isChecked()) {
                    new AlertDialog.Builder(ProminentActivity.this)
                            .setTitle("Alert")
                            .setMessage("You need to select Agree first.")
                            .setPositiveButton("OK", null)
                            .show();
                } else {
startActivity(new Intent(ProminentActivity.this, Login_verifcation.class));
finish();
                }
            }
        });

        btn_sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    startActivity(new Intent(getApplicationContext(), Login_verifcation.class));

            }
        });
        privacyTextClick.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PrivacyActivity.class)));
        termsTextClick.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), TermsActivity.class)));


    }

    @Override
    public void onBackPressed() {

    }
}
