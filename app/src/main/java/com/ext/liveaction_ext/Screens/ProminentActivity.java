package com.ext.liveaction_ext.Screens;

import android.annotation.SuppressLint;
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.prominent_disclosure);

        rdgrp= findViewById(R.id.radiogrpp_dis);
privacyTextClick=findViewById(R.id.policytextradio);
termsTextClick= findViewById(R.id.termtextradio);

        getSupportActionBar().setTitle(R.string.app_name);

        btn_sbmt = findViewById(R.id.btn_submit_Disclosure);

        int selectedId = rdgrp.getCheckedRadioButtonId();

        if (selectedId != -1) {

btn_sbmt.setEnabled(true);


        } else {
            btn_sbmt.setEnabled(false);


        }

        btn_sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    startActivity(new Intent(getApplicationContext(), Login_verifcation.class));

            }
        });
        privacyTextClick.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PrivacyActivity.class)));
        termsTextClick.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), TermsActivity.class)));


    }
}
