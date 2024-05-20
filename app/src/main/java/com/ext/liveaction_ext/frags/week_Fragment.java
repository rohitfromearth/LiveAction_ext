package com.ext.liveaction_ext.frags;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ext.liveaction_ext.Addaptors.CardAdapter;
import com.ext.liveaction_ext.CardItem;
import com.ext.liveaction_ext.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class week_Fragment extends Fragment {
    PieChart pieChart;
    int uid_z;

    int uid = 0;

    TableLayout tbklayout;

    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;

    SharedPreferences sh;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sh = requireActivity().getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment2, container, false);
        tbklayout = view.findViewById(R.id.tabl_lrgrnd_forweek);
        pieChart = view.findViewById(R.id.piechart_for_week);
        recyclerView = view.findViewById(R.id.recyclerView_week);

        Data_show();

        return view;
    }

    private void Data_show() {


        uid = sh.getInt("UID", uid_z);

        Set<String> weekSet = sh.getStringSet("WEEK_LIST", new HashSet<>());
        for (String jsonString : weekSet) {
            try {

                JSONObject jsonObject = new JSONObject(jsonString);

                Random random = new Random();
                int red = random.nextInt(256);
                int green = random.nextInt(256);
                int blue = random.nextInt(256);

                // Format the RGB values into a hexadecimal color code
                String colorCode = String.format("#%02x%02x%02x", red, green, blue);
                String categoryName = jsonObject.optString("category");
                if (!categoryName.equals("Total")) {

                    double pievalue = jsonObject.getDouble("usage_percent_week");
                    double minvalue = jsonObject.getDouble("usage_in_mins_week");

                    float fpi_Value = (float) pievalue;

                    pieChart.addPieSlice(
                            new PieModel(
                                    categoryName,
                                    fpi_Value,
                                    Color.parseColor(colorCode)));

                    addTableRow(categoryName, String.valueOf(minvalue), Color.parseColor(colorCode));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        pieChart.startAnimation();
        // Create the list of card items
        List<CardItem> cardItems = createCardItemsss(); //createCardItems(res);
        // Set up the RecyclerView
        cardAdapter = new CardAdapter(cardItems, requireContext());
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        recyclerView.setAdapter(cardAdapter);

    }

    private List<CardItem> createCardItemsss() {
        List<CardItem> cardItems = new ArrayList<>();
        Set<String> weekSet = sh.getStringSet("WEEK_LIST", new HashSet<>());
        for (String jsonString : weekSet) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                String categoryName = jsonObject.optString("category");
                if (!categoryName.equalsIgnoreCase("Total")) {
                    int logoResId = getLogoResIdForCategory(categoryName);
                    double tsThisDay = jsonObject.optDouble("target_in_mins");
                    double average = jsonObject.optDouble("last_4_week_usage_in_mins");
                    double variance = jsonObject.optDouble("variance_week");

                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    String tsThisDayFormatted = decimalFormat.format(tsThisDay);
                    String averageFormatted = decimalFormat.format(average);
                    String varianceFormatted = decimalFormat.format(variance);

                    String tsText = "TS this week";
                    String averageText = "Average\n(last 4 weeks)";
                    String varianceText = "Variance";

                    String tsThisDays = " " + tsThisDayFormatted;
                    String averages = " " + averageFormatted;
                    String variances = String.valueOf(varianceFormatted);


                    cardItems.add(new CardItem(logoResId, categoryName, tsText, averageText, varianceText, tsThisDays, averages, variances));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cardItems;
    }


    private void addTableRow(String categoryName, String lastWeek, int color_code) {
 TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.legend_data, null);

        TextView tvCategory = row.findViewById(R.id.tv_legend_Category);
        TextView tvLastWeek = row.findViewById(R.id.tv_legend_per);
        TextView tvLegendColor = row.findViewById(R.id.tv_legend_color);

        tvCategory.setText(categoryName);
        tvLastWeek.setText(lastWeek + "hr");
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
                return R.drawable.music1;
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