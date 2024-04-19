package com.ext.liveaction_ext.Screens;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ext.liveaction_ext.R;

public class ProminentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.prominent_disclosure);

        getSupportActionBar().setTitle(R.string.app_name);

    }
}
