package com.ext.liveaction_ext.Screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.ext.liveaction_ext.FAQActivity;
import com.ext.liveaction_ext.R;

public class FaQ extends Fragment {
    CardView btn_dash;
    TextView clickFaq;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_fa_q, container, false);
        btn_dash = view.findViewById(R.id.mange_btn);
        clickFaq = view.findViewById(R.id.clickFaq);

        btn_dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().alpha(1f).setDuration(200);
                    }
                }).start();
                startActivity(new Intent(getContext(), Dashboard.class));
            }
        });

        clickFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startYourActivity();
                startActivity(new Intent(getContext(), FAQActivity.class));
            }
        });


        return view;
    }
    // Method to start YourActivity

}