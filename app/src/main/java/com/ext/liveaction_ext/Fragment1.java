package com.ext.liveaction_ext;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Fragment1 extends Fragment {
    PieChart pieChart;
    DummyData dumdta = new DummyData();
    int uid_z;
    String firebaseToke = "";
    int uid = 0;
    Conn_service conn = new Conn_service();
    TableLayout tbklayout;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private CardAdapter cardAdapter;
    private ProgressBar loadingProgress;
    boolean isProgressVisible = false;
    String categoryName;
    Context context;
    SharedPreferences sh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment1, container, false);

        tbklayout = view.findViewById(R.id.tabl_lrgrnd);
        pieChart = view.findViewById(R.id.piechart);
        recyclerView = view.findViewById(R.id.recyclerView);
        loadingProgress = view.findViewById(R.id.loading);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser use = mAuth.getCurrentUser();

        if (use != null) {
            use.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                firebaseToke = task.getResult().getToken();
                                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("firebaseToken", firebaseToke);
                                myEdit.apply();
                                Data_Show(firebaseToke);
                            }
                        }

                    });
        }
        return view;
    }

    private void Data_Show(String firebaseToken) {
        try {

            loadingProgress.setVisibility(View.VISIBLE);
             sh = requireActivity().getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
            uid = sh.getInt("UID", uid_z);

            //april
          Set<String> daySet = sh.getStringSet("DAY_LIST", new HashSet<>());

          /* for (String day : daySet) {
               //Log.d("DaySet", "fragment1 day: " + day);
           }
            Set<String> daSet = sh.getStringSet("WEEK_LIST", new HashSet<>());

            for (String day : daSet) {
               // Log.d("DaySet", "fragment1 week: " + day);
            }*/
            //april

            //String res = conn.pack_rule("/usageStats/getUsageData?userId=" + 21, firebaseToken);

            String usageData = sh.getString("key_usage_data", ""); // Provide default value if key not found

            String resultdummy = dumdta.Data_for_dash();
            JSONObject jsonncheck = new JSONObject(usageData);

            boolean showDummy = jsonncheck.getBoolean("showDummy");
            if (showDummy) {

                piecreate(resultdummy);
                List<CardItem> cardItems = createCardItems(usageData);
                cardAdapter = new CardAdapter(cardItems, requireContext());
                recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                recyclerView.setAdapter(cardAdapter);

            } else {
                piecreate(usageData);
                List<CardItem> cardItems = createCardItemsss();
                cardAdapter = new CardAdapter(cardItems, requireContext());
                recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                recyclerView.setAdapter(cardAdapter);
            }
            loadingProgress.setVisibility(View.GONE);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    private List<CardItem> createCardItemsss() {
        List<CardItem> cardItems = new ArrayList<>();
        Set<String> daySet = sh.getStringSet("DAY_LIST", new HashSet<>());
        for (String jsonString : daySet) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                // String categoryName = item.getString("category");
                String categoryName = jsonObject.optString("category");
                int logoResId = getLogoResIdForCategory(categoryName);
                // int tsthisday = item.getInt("usage_in_mins_day");
                int tsthisday = jsonObject.optInt("usage_in_mins_day");
                // int average = item.getInt("last_7_day_usage_in_mins");
                int average = jsonObject.optInt("last_7_day_usage_in_mins");
                //int variance = item.getInt("variance_day");
                int variance = jsonObject.getInt("variance_day");
                Log.d("catee", "cName: " + categoryName);
                String tsText = " TS this day";
                String averageText = " Average\n(last 7 days)";
                String varianceText = " Variance";
                String tsthisdays = " "+ tsthisday;
                String averages = " " + average;
                String variences = String.valueOf(variance);
                cardItems.add(new CardItem(logoResId, categoryName, tsText, averageText, varianceText,tsthisdays, averages, variences));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cardItems;
    }
    private void piecreate() {

        Set<String> daySet = sh.getStringSet("DAY_LIST", new HashSet<>());
        for (String jsonString : daySet) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                Random random = new Random();
                int red = random.nextInt(256);
                int green = random.nextInt(256);
                int blue = random.nextInt(256);

                // Format the RGB values into a hexadecimal color code
                String colorCode = String.format("#%02x%02x%02x", red, green, blue);

              //  String categoryName = jsonObject.getString("category");
                String categoryName = jsonObject.optString("category");
                if (!categoryName.equals("Total")) {

                    double pievalue = jsonObject.getDouble("usage_percent_day");
                    double minvalue = jsonObject.getDouble("usage_in_mins_day");

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
           // To animate the pie chart
        pieChart.startAnimation();
    }

    private List<CardItem> createCardItems(String res) {
        List<CardItem> cardItems = new ArrayList<>();
        Set<String> daySet = sh.getStringSet("DAY_LIST", new HashSet<>());
        for (String jsonString : daySet) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
               // String categoryName = item.getString("category");
                String categoryName = jsonObject.optString("category");
                int logoResId = getLogoResIdForCategory(categoryName);
               // int tsthisday = item.getInt("usage_in_mins_day");
                int tsthisday = jsonObject.optInt("usage_in_mins_day");
               // int average = item.getInt("last_7_day_usage_in_mins");
                int average = jsonObject.optInt("last_7_day_usage_in_mins");
                //int variance = item.getInt("variance_day");
                int variance = jsonObject.getInt("variance_day");
                Log.d("catee", "cName: " + categoryName);
                String tsText = " TS this day";
                String averageText = " Average\n(last 7 days)";
                String varianceText = " Variance";
                String tsthisdays = " "+ tsthisday;
                String averages = " " + average;
                String variences = String.valueOf(variance);
                cardItems.add(new CardItem(logoResId, categoryName, tsText, averageText, varianceText,tsthisdays, averages, variences));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

      /*  try {
            JSONObject jsonObject = new JSONObject(res);

            JSONArray jsonArray = jsonObject.getJSONArray("day");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                String categoryName = item.getString("category");

                Log.d("using res",categoryName);
                int logoResId = getLogoResIdForCategory(categoryName);
                int tsthisday = item.getInt("usage_in_mins_day");
                int average = item.getInt("last_7_day_usage_in_mins");
                int variance = item.getInt("variance_day");

                String tsText = " TS this day";
                String averageText = " Average\n(last 7 days)";
                String varianceText = " Variance";

                String tsthisdays = " "+ tsthisday;
                String averages = " " + average;
                String variences = String.valueOf(variance);

                cardItems.add(new CardItem(logoResId, categoryName, tsText, averageText, varianceText,tsthisdays, averages, variences));

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Execption", String.valueOf(e));
        }*/

        return cardItems;
    }
    private void piecreate(String res) {
        try {
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray("day");
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

                    double pievalue = item.getDouble("usage_percent_day");
                    double minvalue = item.getDouble("usage_in_mins_day");

                    float fpi_Value = (float) pievalue;

                    pieChart.addPieSlice(
                            new PieModel(
                                    categoryName,
                                    fpi_Value,
                                    Color.parseColor(colorCode)));

                    addTableRow(categoryName, String.valueOf(minvalue), Color.parseColor(colorCode));


                }
            }
            loadingProgress.setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Exception", String.valueOf(e));
        }

        // To animate the pie chart
        pieChart.startAnimation();
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