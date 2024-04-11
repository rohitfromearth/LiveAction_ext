package com.ext.liveaction_ext.Screens;

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
    TextView clickFaq, privacy, terms;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_fa_q, container, false);
        btn_dash = view.findViewById(R.id.mange_btn);
        clickFaq = view.findViewById(R.id.clickFaq);
        privacy = view.findViewById(R.id.privacyPolicy);
        terms = view.findViewById(R.id.termsConditions);

        btn_dash.setOnClickListener(view1 -> {
            view1.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view1.animate().alpha(1f).setDuration(200)).start();
            startActivity(new Intent(getContext(), Dashboard.class));
        });
        clickFaq.setOnClickListener(view12 -> {

            startActivity(new Intent(getContext(), FAQActivity.class));
        });
        privacy.setOnClickListener(view13 -> {
            // startYourActivity();
            startActivity(new Intent(getContext(), PrivacyActivity.class));
        });
        terms.setOnClickListener(view14 -> {

            startActivity(new Intent(getContext(), TermsActivity.class));
        });
        return view;
    }
    // Method to start YourActivity

}