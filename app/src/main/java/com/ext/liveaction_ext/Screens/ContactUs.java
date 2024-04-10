package com.ext.liveaction_ext.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.ext.liveaction_ext.R;

public class ContactUs extends Fragment {
    CardView btn_dash;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_contact_us, container, false);
        btn_dash = view.findViewById(R.id.dashboard_connect);
        btn_dash.setOnClickListener(view1 -> {

            view1.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view1.animate().alpha(1f).setDuration(200)).start();
            startActivity(new Intent(getContext(), Dashboard.class));
        });
        return view;
    }
}