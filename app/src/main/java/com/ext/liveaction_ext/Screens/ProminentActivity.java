package com.ext.liveaction_ext.Screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ext.liveaction_ext.R;

public class ProminentActivity extends AppCompatActivity {
CardView btn_sbmt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.prominent_disclosure);

        getSupportActionBar().setTitle(R.string.app_name);

        btn_sbmt= findViewById(R.id.btn_submit_Disclosure);


        btn_sbmt.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login_verifcation.class)));

    }
}
