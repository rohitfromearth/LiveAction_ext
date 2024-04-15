package com.ext.liveaction_ext.Screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ext.liveaction_ext.FAQActivity;
import com.ext.liveaction_ext.QandA;
import com.ext.liveaction_ext.QuestionAnswerAdapter;
import com.ext.liveaction_ext.R;

import java.util.ArrayList;
import java.util.List;

public class FaQ extends Fragment {
    CardView btn_dash;
    TextView clickFaq, privacy, terms;

    private RecyclerView recyclerView;
    private QuestionAnswerAdapter adapter;
    private int lastExpandedPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_fa_q, container, false);
        btn_dash = view.findViewById(R.id.dashboard_btn_faq);




        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        List<QandA> itemList = generateSampleData();

        adapter = new QuestionAnswerAdapter(generateSampleData());
        recyclerView.setAdapter(adapter);


//            if (lastExpandedPosition != -1 && lastExpandedPosition != position) {
//                adapter.collapse(lastExpandedPosition);
//            }
//            lastExpandedPosition = position;
//

        btn_dash.setOnClickListener(view1 -> {
            view1.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view1.animate().alpha(1f).setDuration(200)).start();
            startActivity(new Intent(getContext(), Dashboard.class));
        });


        return view;
    }
    // Method to start YourActivity
    private List<QandA> generateSampleData() {
        List<QandA> itemList = new ArrayList<>();
        itemList.add(new QandA("What is Android?", "Android is a mobile operating system developed by Google."));
        itemList.add(new QandA("What is RecyclerView?", "RecyclerView is a flexible view for providing a limited window into a large data set."));
        itemList.add(new QandA("What is an adapter?", "An adapter is a bridge between the UI components and the data source that helps to fill data into the UI components."));
        return itemList;
    }
}