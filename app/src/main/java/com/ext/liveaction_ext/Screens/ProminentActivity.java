package com.ext.liveaction_ext.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ext.liveaction_ext.R;

public class ProminentActivity extends AppCompatActivity {
    CardView btn_sbmt;
    RadioButton chek_agree, check_disAgree;
    TextView privacyTextClick, termsTextClick;
    RadioGroup radioGroup;
    private boolean isCardViewVisible = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prominent_disclosure);
        //privacyTextClick = findViewById(R.id.policytextradio);
    //    termsTextClick = findViewById(R.id.termtextradio);
        getSupportActionBar().setTitle(R.string.app_name);
        btn_sbmt = findViewById(R.id.btn_submit_Disclosure);

        radioGroup = findViewById(R.id.radiogrppDisclosure);

        btn_sbmt = findViewById(R.id.btn_submit_Disclosure);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            btn_sbmt.setVisibility(View.GONE);
        } else {
            btn_sbmt.setVisibility(View.VISIBLE);
        }
       /* if (isCardViewVisible) {
            btn_sbmt.setVisibility(View.GONE);
        } else {
            btn_sbmt.setVisibility(View.VISIBLE);
        }*/
        isCardViewVisible = !isCardViewVisible;
        btn_sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (true) {

                    startActivity(new Intent(getApplicationContext(), Login_verifcation.class));
                }
            }
        });
//        privacyTextClick.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PrivacyActivity.class)));
//        termsTextClick.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), TermsActivity.class)));
    }


}