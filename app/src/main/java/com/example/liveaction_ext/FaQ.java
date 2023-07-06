package com.example.liveaction_ext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class FaQ extends AppCompatActivity {
    CardView btn_dash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_q);
        btn_dash = findViewById(R.id.mange_btn);
        btn_dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().alpha(1f).setDuration(200);
                    }
                }).start();
                startActivity(new Intent(FaQ.this, Chart_page.class));
            }
        });
    }
}