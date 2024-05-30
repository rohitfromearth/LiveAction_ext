package com.ext.liveaction_ext.Screens;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.ext.liveaction_ext.R;

public class ProminentActivity extends AppCompatActivity {
CardView btn_sbmt;
CheckBox chek_agree;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.prominent_disclosure);
        getSupportActionBar().setTitle(R.string.app_name);
        btn_sbmt= findViewById(R.id.btn_submit_Disclosure);
        chek_agree= findViewById(R.id.chk_for_aggree);

        btn_sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkk = chek_agree.isChecked();

                if(checkk){

                startActivity(new Intent(getApplicationContext(), Login_verifcation.class));
                finish();
                }
            }
        });


    }
}
