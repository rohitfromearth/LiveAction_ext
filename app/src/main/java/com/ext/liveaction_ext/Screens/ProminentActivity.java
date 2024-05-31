package com.ext.liveaction_ext.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ext.liveaction_ext.R;

public class ProminentActivity extends AppCompatActivity {
    CardView btn_sbmt;
    RadioButton chek_agree, check_disAgree;
    TextView privacyTextClick, termsTextClick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.prominent_disclosure);

        getSupportActionBar().setTitle(R.string.app_name);

        btn_sbmt = findViewById(R.id.btn_submit_Disclosure);

        chek_agree = findViewById(R.id.radio_for_agree);
        check_disAgree = findViewById(R.id.radio_for_disAgree);
        privacyTextClick = findViewById(R.id.privacyIdd);
        termsTextClick = findViewById(R.id.termsIdd);

        btn_sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkk = chek_agree.isChecked();

                if (checkk) {
                    startActivity(new Intent(getApplicationContext(), Login_verifcation.class));
                }
            }
        });
        privacyTextClick.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PrivacyActivity.class)));
        termsTextClick.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), TermsActivity.class)));


    }
}
