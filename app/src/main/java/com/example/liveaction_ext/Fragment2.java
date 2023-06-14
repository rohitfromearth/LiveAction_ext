package com.example.liveaction_ext;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    int  uid_z;
    String  firebaseToken="";
    int uid =  0;
    Conn_service conn = new Conn_service();
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    TableLayout tbklayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment2, container, false);
        tbklayout =view.findViewById(R.id.tabl_lrgrnd_forweek);
        pieChart = view.findViewById(R.id.piechart_for_week);
        SharedPreferences sh = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);


        firebaseToken= sh.getString("firebaseToken","");

        uid = sh.getInt("UID", uid_z);
        String res = conn.pack_rule("/usageStats/getUsageData?screen=dashboard&duration=week&userId="+uid, firebaseToken);
        //onPostExecute(res);
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
                if(!categoryName.equals("Total")) {
                    int pievalue = item.getInt("usage_percent");
                    Log.e("random", String.valueOf(pievalue));
                    pieChart.addPieSlice(
                            new PieModel(
                                    categoryName,
                                    pievalue,
                                    Color.parseColor(colorCode)));

                    addTableRow(categoryName, String.valueOf(pievalue), Color.parseColor(colorCode));
                }
            }
        }catch (JSONException e) {
            e.printStackTrace();
            Log.e("Exception", String.valueOf(e));
        }

        // To animate the pie chart
        pieChart.startAnimation();
        recyclerView = view.findViewById(R.id.recyclerView_week);

        // Create the list of card items
        List<CardItem> cardItems = createCardItems(res);

        // Set up the RecyclerView
        cardAdapter = new CardAdapter(cardItems, requireContext());
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        recyclerView.setAdapter(cardAdapter);


        return view;
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
                int average= item.getInt("last_4_week_usage_in_mins");
                int variance = item.getInt("variance");
                String tsthisdays= "TS this week  "+tsthisday;
                String averages ="Average\n(last 4 weeks)  "+ average;
                String variences= String.valueOf(variance);




                cardItems.add(new CardItem(logoResId, categoryName, tsthisdays, averages, variences));
//                addTableRow(categoryName, lastWeek, cumulative,pointslastweek,pointscumm);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Execption", String.valueOf(e));
        }
        ////


        // Add your card items here
        // Example:
//        cardItems.add(new CardItem( R.drawable.logo_mails,"Mails", "Info 1", "Info 2", "Info 3"));
//        cardItems.add(new CardItem( R.drawable.logo_games,"Games", "Info 4", "Info 5", "Info 6"));
//        cardItems.add(new CardItem( R.drawable.logo_meetings,"E-meetings", "Info 4", "Info 5", "Info 6"));
//        cardItems.add(new CardItem(R.drawable.logo_music, "Music", "Info 4", "Info 5", "Info 6"));
//        cardItems.add(new CardItem(R.drawable.logo_video, "OTT/Video", "Info 4", "Info 5", "Info 6"));
//        cardItems.add(new CardItem(R.drawable.logo_social, "Social", "Info 4", "Info 5", "Info 6"));
//        cardItems.add(new CardItem(R.drawable.logo_travel, "Travel", "Info 4", "Info 5", "Info 6"));
//        cardItems.add(new CardItem(R.drawable.logo_fitness, "Fitness", "Info 4", "Info 5", "Info 6"));
//        cardItems.add(new CardItem( R.drawable.logo_learn,"Learn", "Info 4", "Info 5", "Info 6"));
//        cardItems.add(new CardItem(R.drawable.logo_sleep, "Sleep", "Info 4", "Info 5", "Info 6"));
//        cardItems.add(new CardItem(R.drawable.logo_shopping, "Shop", "Info 4", "Info 5", "Info 6"));
//        cardItems.add(new CardItem(R.drawable.logo_others, "Others", "Info 4", "Info 5", "Info 6"));


        // ...

        return cardItems;
    }
    private void onPostExecute(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("result");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    String categoryName = item.getString("category");
                    String lastWeek = item.getString("usage_percent");


                    // addTableRow(categoryName, lastWeek);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void addTableRow(String categoryName, String lastWeek, int color_code) {
//        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.table_row_item, tableLayout, false);

        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.legend_data, null);

        TextView tvCategory = row.findViewById(R.id.tv_legend_Category);
        TextView tvLastWeek = row.findViewById(R.id.tv_legend_per);

        TextView tvLegendColor = row.findViewById(R.id.tv_legend_color);


        tvCategory.setText(categoryName);
        tvLastWeek.setText(lastWeek+"%");

        tvLegendColor.setBackgroundColor(color_code);


        tbklayout.addView(row);
    }

    private int getLogoResIdForCategory(String category) {
        switch (category) {
            case "Emails":
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