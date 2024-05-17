package com.ext.liveaction_ext.Screens;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    Dialog dialog;
    Context context;

    private static final String PERMISSION_DIALOG_TAG = "permission_dialog";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_fa_q, container, false);
        btn_dash = view.findViewById(R.id.dashboard_btn_faq);

        privacy = view.findViewById(R.id.privacyId);
        terms = view.findViewById(R.id.termsId);


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//        List<QandA> itemList = generateSampleData();

        adapter = new QuestionAnswerAdapter(generateSampleData());
        recyclerView.setAdapter(adapter);

        boolean showPermissionDialog = getActivity().getIntent().getBooleanExtra("show_permission_dialog", false);
        Log.e("showPermissionDialog", String.valueOf(showPermissionDialog));
        if (showPermissionDialog) {
            // Show permission dialog
            showPermissionDialog();
        }
//            if (lastExpandedPosition != -1 && lastExpandedPosition != position) {
//                adapter.collapse(lastExpandedPosition);
//            }
//            lastExpandedPosition = position;
//


        btn_dash.setOnClickListener(view1 -> {
            view1.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view1.animate().alpha(1f).setDuration(200)).start();
            startActivity(new Intent(getContext(), Dashboard.class));
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
    private List<QandA> generateSampleData() {
        List<QandA> itemList = new ArrayList<>();
        itemList.add(new QandA("What does this app do?", "Lifeactions measures your behaviour across apps and websites and the time that you spend online. It helps you understand your usage across different kinds of apps e.g. social media, learning, entertainment, etc. This helps you understand your digital life and identify the opportunities to prioritize, reduce unproductive time and optimize your digital life. It also helps you craft your digital life and time that you invest. For instance, you can identify the apps / activities that you would like to cut down time on – e.g. on social media. Lifeactions allows you to set personal goals and targets and then shows you how successful you were. It not just measures your performance of your goals, but also celebrates your achievement. It gives you rewards and incentives on achieving your goals."));
        itemList.add(new QandA("How does it help me?", "The app tells you how you spend your time on your phone. It profiles the activities that you undertake during different day parts and the time you invest in them. It is a mirror to your digital life. Using your own personal behaviour, the app provides you an opportunity to improve and optimize the time you invest in various activities and missions. For instance, you can identify the apps / activities that you would like to cut down time on – e.g. on social media. Lifeactions allows you to set personal goals and targets and then shows you how successful you were. It not just measures your performance of your goals, but also celebrates your achievement. It gives you rewards and incentives on achieving your goals."));
        itemList.add(new QandA("How can I manage my actions?", "For any performance improvement plan, one needs data. Our app measures the time you spend on various apps and websites. It thus catalogues your digital behaviour and how you use the phone. It then collates various activities under different actions/ groups e.g. time spent on communication, shopping, social media, learning, business etc. It thus gives you a break down of how you invest your time online. Using your own personal behaviour, the app provides you an opportunity to improve and optimize the time you invest in various activities and missions. For instance, you can identify the apps / activities that you would like to cut down time on – e.g. on social media. Lifeactions allows you to set personal goals and targets and then shows you how successful you were. It not just measures your performance of your goals, but also celebrates your achievement. It gives you rewards and incentives on achieving your goals. Lifeactions is your partner and coach which helps you improve and optimize your digital life."));
        itemList.add(new QandA("How do I get rewarded?", "We see our relationship with you as a partnership. As a token of our appreciation, we reward you for being a panelist. You receive a reward equivalent of Rs 10 for each week that you continue being on our panel. In addition, to motivate you towards meeting your personal stated goals and to celebrate achieving them, we reward you with additional incentives. For each goal that you achieve in a given period, we credit an additional incentive amounting to Rs 25. To ensure full transparency, all the incentives earned by you, are clearly shown in your personal dashboard. As soon as you have accumulated incentives worth Rs 250, you can start redeeming them. We offer your various ways of redeeming your incentives – cash, data packs, shopping vouchers, etc."));
        itemList.add(new QandA("What personal data do you collect?", "When you join the panel, we collect the following information about you: Your stated age, Gender, Mobile number – for verification, Location (approximate) – e.g. name of the city, First and Last Name – only for use in your personal dashboard. None of the above are ever used by us for analyses or shared with any third party. We subscribe to and adhere to the highest form of data privacy and therefore no personally identifiable information is ever made available or used or shared with anybody including our own team. We are fully compliant with GDPR guidelines on data privacy and confidentiality. We also measure your usage of apps, websites, and search engines. We use this data to map your digital behaviour. This data is never analyzed, used, or shared at an individual level. We aggregate this data to understand the usage behavior of consumers of similar profiles."));
        itemList.add(new QandA("What do you do with the data collected?", "The data of all our panelists is stored in a secure server with highest levels of security. Each panelist is identified by a unique alphanumeric number to ensure that is totally anonymous i.e. there is no personally identifiable information that is available for them. It is then aggregated and analyzed to understand how consumers use the internet. We study their usage behavior and draw insights which help brands and service providers to get a peek into where their consumers are, what do they spend their time on, what kind of content works for them, etc."));
        itemList.add(new QandA("How is my privacy protected?", "Data of our panelists is kept anonymous i.e. there is no personally identifiable information that is available for them. No personal data is ever used by us for analyses or shared with any third party. We subscribe to and adhere to the highest form of data privacy and therefore no personally identifiable information is ever made available or used or shared with anybody including our own team. We are fully compliant with GDPR guidelines on data privacy and confidentiality."));
        itemList.add(new QandA("How can I remove the app?", "It is very simple – you can uninstall the app and remove it from your devices. Once the app is uninstalled, we do not have access to your phone. Thus, the data coming to us from you is stopped immediately. In case you wish to rejoin the panel, you need to send us a request. We will send you a unique joining link which you need to register yourself."));
        itemList.add(new QandA("How can I unsubscribe and how can I ask for removal of my data?", "For unsubscribing to the app, simply uninstall the app and delete it. This would stop us from having access to your phone as well as to the data coming to us from you. In case you wish us to remove your data, please write to us at support@lifeactions.ai. We will confirm the deletion within seven working days. In case you have any further questions please write to Lifeactionstrack@gmail.com."));
        return itemList;
    }

    public void showPermissionDialog() {
        // Show permission dialog here
        // For example:
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_for_perrmisson);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        /*new AlertDialog.Builder(getContext())
                .setTitle("Permission Dialog")
                .setMessage("This is a permission dialog.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle OK button click if needed
                    }
                })
                .show();*/
    }
}