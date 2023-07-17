package com.example.liveaction_ext;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fragment2 extends Fragment {
    PieChart pieChart;
    int uid_z;
    //    private FirebaseAuth FirebaseAuh;
    String firebaseToken = "";
    String firebaseToke = "";
    int uid = 0;
    Conn_service conn = new Conn_service();
    TableLayout tbklayout;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment2, container, false);
        tbklayout = view.findViewById(R.id.tabl_lrgrnd_forweek);
        pieChart = view.findViewById(R.id.piechart_for_week);
        recyclerView = view.findViewById(R.id.recyclerView_week);
        mAuth = FirebaseAuth.getInstance();
        Log.e("mAuth3", String.valueOf(mAuth));
        FirebaseUser use = mAuth.getCurrentUser();
        Log.e("mAuth4", String.valueOf(use));

        if (use != null) {
            use.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                firebaseToke = task.getResult().getToken();
                                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("firebaseToken", firebaseToke);
                                myEdit.apply();
                                Data_show(firebaseToke);
                            }


                        }

                    });

        }


        return view;
    }

    private void Data_show(String firebaseToken) {
        SharedPreferences sh = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);

        uid = sh.getInt("UID", uid_z);
        String res = conn.pack_rule("/usageStats/getUsageData?screen=dashboard&duration=week&userId=" + uid, firebaseToken);

        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                Random random = new Random();
                int red = random.nextInt(256);
                int green = random.nextInt(256);
                int blue = random.nextInt(256);

                // Format the RGB values into a hexadecimal color code
                String colorCode = String.format("#%02x%02x%02x", red, green, blue);

                String categoryName = item.getString("category");
                if (!categoryName.equals("Total")) {
//            String Pieval=     item.getString("usage_percent");
                    double pievalue = item.getDouble("usage_percent");
                    double minvalue = item.getDouble("usage_in_mins");
                    float fpi_Value = (float) pievalue;
                    Log.e("random", String.valueOf(pievalue));
                    pieChart.addPieSlice(
                            new PieModel(
                                    categoryName,
                                    fpi_Value,
                                    Color.parseColor(colorCode)));

                    addTableRow(categoryName, String.valueOf(minvalue), Color.parseColor(colorCode));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Exception", String.valueOf(e));
        }

        // To animate the pie chart
        pieChart.startAnimation();


        // Create the list of card items
        List<CardItem> cardItems = createCardItems(res);

        // Set up the RecyclerView
        cardAdapter = new CardAdapter(cardItems, requireContext());
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(cardAdapter);

    }

    private List<CardItem> createCardItems(String res) {
        List<CardItem> cardItems = new ArrayList<>();


        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String categoryName = item.getString("category");
                int logoResId = getLogoResIdForCategory(categoryName);
                int tsthisday = item.getInt("usage_in_mins");
                int average = item.getInt("last_4_week_usage_in_mins");
                int variance = item.getInt("variance");

                String tsText = " TS this week";
                String averageText = " Average\n(last 4 weeks)";
                String varianceText = " Variance";

                String tsthisdays = " " + tsthisday;
                String averages = " " + average;
                String variences = String.valueOf(variance);


                cardItems.add(new CardItem(logoResId, categoryName, tsText, averageText, varianceText, tsthisdays, averages, variences));
//                addTableRow(categoryName, lastWeek, cumulative,pointslastweek,pointscumm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Execption", String.valueOf(e));
        }


        return cardItems;
    }

    private void addTableRow(String categoryName, String lastWeek, int color_code) {
//        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.table_row_item, tableLayout, false);

        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.legend_data, null);

        TextView tvCategory = row.findViewById(R.id.tv_legend_Category);
        TextView tvLastWeek = row.findViewById(R.id.tv_legend_per);

        TextView tvLegendColor = row.findViewById(R.id.tv_legend_color);


        tvCategory.setText(categoryName);

        double str1 = Double.parseDouble(lastWeek);
        float hrs = (float) (str1/60);
        String strHrs = String.format("%.2f",hrs);
        tvCategory.setText(categoryName);
        tvLastWeek.setText(strHrs + "Hr");

        tvLegendColor.setBackgroundColor(color_code);


        tbklayout.addView(row);
    }

    private int getLogoResIdForCategory(String category) {
        switch (category) {
            case "Email":
                return R.drawable.logo_mail;
            case "Games":
                return R.drawable.logo_games;
            case "e-Meetings":
                return R.drawable.logo_meetings;
            case "Music":
                return R.drawable.logo_music;
            case "OTT/Video":
                return R.drawable.logo_video;
            case "Social":
                return R.drawable.logo_social;
            case "Travel":
                return R.drawable.logo_travel;
            case "Fitness":
                return R.drawable.logo_fitness;
            case "Learn":
                return R.drawable.logo_learn;
            case "Idle":
                return R.drawable.logo_sleep;
            case "Shopping":
                return R.drawable.logo_shopping;
            case "Others":
                return R.drawable.logo_others;
            case "Entertainment":
                return R.drawable.logo_enter;

            case "Finance":
                return R.drawable.logo_finance;
            default:
                return 0;
        }
    }


}