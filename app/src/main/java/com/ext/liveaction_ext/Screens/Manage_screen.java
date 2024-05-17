package com.ext.liveaction_ext.Screens;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ext.liveaction_ext.R;
import com.ext.liveaction_ext.service_clas.Conn_service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Manage_screen extends Fragment {
    Conn_service servic = new Conn_service();
    TextView tv_commnt;
    ArrayList<Integer> valuesperList = new ArrayList<>();
    ArrayList<Integer> valueshrList = new ArrayList<>();// Create an ArrayList to store the values
    JSONObject jsonBody = new JSONObject();
    int uid_z;
    int uid = 0;
    String firebaseToken = "";
    boolean target_edi = false;
    String firebaseToke = "";
    JSONArray jsonAy = new JSONArray();
    TextView tv_btn;
    CardView btn_sv,btn_dash;
    int valueperInt,valuehrInt;
    private final ArrayList<EditText> etList = new ArrayList<>();
    private LinearLayout floatingViewContainer;
    private View floatingView;
    private TableLayout tableLayout;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private FirebaseAuth mAuth;
    private float initialTouchY;
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_manage_screen, container, false);
        btn_sv = view.findViewById(R.id.save_btn);
        tv_btn = view.findViewById(R.id.savetargt);
        tableLayout = view.findViewById(R.id.tableLayout);
        floatingViewContainer = view.findViewById(R.id.floatingViewContainer);
        tv_commnt = view.findViewById(R.id.comment_live_act);
        mAuth = FirebaseAuth.getInstance();
btn_dash= view.findViewById(R.id.dashboard_btn_mng);
        FirebaseUser use = mAuth.getCurrentUser();


        if (use != null) {
            use.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        firebaseToke = task.getResult().getToken();
                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("LifeSharedPref", MODE_PRIVATE);

                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("firebaseToken", firebaseToke);
                        myEdit.apply();
                    }
                }
            });
        }
        SharedPreferences sh = getContext().getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
        uid = sh.getInt("UID", uid_z);
        firebaseToken = sh.getString("firebaseToken", "");


        // Inflate the floating view layout
        inflater = LayoutInflater.from(getContext());
        floatingView = inflater.inflate(R.layout.layout_bell, floatingViewContainer, false);

        // Add touch listener to make the view movable
        floatingView.setOnTouchListener(new FloatingViewTouchListener());

        // Add the floating view to the container
        floatingViewContainer.addView(floatingView);

        String res = servic.pack_rule("/usageStats/manageData?duration=week&userId=" + uid, firebaseToken);

        Log.e("res","/usageStats/manageData : "+res);
        boolean edit = onPostExecute(res);
        if (edit) {
            tv_btn.setText("Save Target");
        }
        String res_comnt = servic.pack_rule("/usageStats/comments/" + uid, firebaseToken);
        Log.e("res_comnt","/usageStats/comments/ : "+res_comnt);

        try {
            JSONObject jsonObj = new JSONObject(res_comnt);
            String Comments_live_action = jsonObj.getString("result");
            tv_commnt.setText(Comments_live_action);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        btn_sv.setOnClickListener(view1 -> {

            view1.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view1.animate().alpha(1f).setDuration(200)).start();
            if (edit) {
                for (EditText editTextforhr : etList) {
                    String value = editTextforhr.getTag().toString();

                    String editTextValuehr = editTextforhr.getText().toString();

                    if (editTextValuehr.equals("")) { // detect an empty string and set it to "0" instead
                        editTextValuehr = "0";
                    }
                    int indexOfUnderscore = value.indexOf("_");

                    String usaage_id = value.substring(0, indexOfUnderscore);


                    try {

                        JSONObject financeObj = new JSONObject();
                        financeObj.put("userId", uid);
                        financeObj.put("category", usaage_id);

                        if (value.contains("_actual_hr")) {
                            financeObj.put("targetInMins", editTextValuehr);
                            valuehrInt = Integer.parseInt(editTextValuehr.trim());
                            valueshrList.add(valuehrInt);

                        } else {
                            financeObj.put("targetInPercent", editTextValuehr);
                            valueperInt = Integer.parseInt(editTextValuehr.trim());
                            valuesperList.add(valueperInt);
                        }
                        jsonAy.put(financeObj);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }
                try {
                    jsonBody.put("data", jsonAy);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                boolean validateper = validationonper(valuesperList);
                boolean validatehr= validationonhr(valueshrList);
                if (validateper && validatehr) {
                    addData(jsonBody);
                    //startActivity(new Intent(getContext(), Achieve.class));
                    Fragment go_next = new Achieve();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();
                    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                    assert actionBar != null;
                    actionBar.setTitle("Achievements");
                    // getActivity().getActionBar().setTitle("Achievements");
                } else {
                    valuesperList.clear();
                    valueshrList.clear();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    // Set the title and message for the dialog
                    builder.setTitle("Validation Error");
                    builder.setMessage("Total of target in hour as well " + " in percentage should be 100 or less than 100");

                    // Set a positive button and its click listener
                    builder.setPositiveButton("OK", (dialog, which) -> {
                        // Handle the click event (if needed)
                    });

                    // Create and show the alert dialog
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }


            } else {
                //startActivity(new Intent(getContext(), Achieve.class));
                Fragment go_next = new Achieve();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();
                ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                actionBar.setTitle("Achievements");
                // getActivity().getActionBar().setTitle("Achievements");
            }
        });
        btn_dash.setOnClickListener(v -> startActivity(new Intent(getContext(), Dashboard.class)));
        return view;
    }

    private boolean onPostExecute(String result) {
        boolean editable = false;

        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String target_hr = " ";
                    String target_per = "";
                    JSONObject item = jsonArray.getJSONObject(i);
                    String categoryName = item.getString("category");
                    int usage_id = item.getInt("usacm_id");
                    String lastWeek = item.getString("usage_in_mins");
                    String cumulative = item.getString("usage_percent");

                    String targetinmin = item.getString("target_in_mins");
                    String targetinper = item.getString("target_in_percent");

                    String pointslastweek = item.getString("target_variance");

                    if (targetinmin.equals("0") && targetinper.equals("0")) {

                        target_hr = "";
                        target_per = " " ;
                    } else if (targetinmin.equals("0") && !targetinper.equals("0")) {
                        target_hr = "";
                        target_per = targetinper + "%";
                    }   else if (!targetinmin.equals("0")  && targetinper.equals("0")) {
                        target_hr = targetinmin;
                        target_per = " " ;
                    }else{
                        target_hr = targetinmin;
                        target_per = targetinper + "%";
                    }
                    target_edi = item.getBoolean("target_editable");


                    addTableRow(usage_id, target_hr, target_per, categoryName, lastWeek, cumulative, pointslastweek, target_edi);
                    if (usage_id == 1) {
                        editable = target_edi;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return editable;
    }

    private boolean validationonper(ArrayList<Integer> valuesList) {
        int sum = 0;
        for (int number : valuesList) {
            sum += number;
        }
        return sum <= 100;
    }
    private boolean validationonhr(ArrayList<Integer> valuesList) {
        int sum = 0;
        for (int number : valuesList) {
            sum += number;
        }
        return sum <= 168;
    }

    private void addTableRow(int usageid, String target_hr, String target_per, String categoryName, String lastWeek, String cumulative, String pointslastWe, boolean edit_view) {


        TableLayout row = (TableLayout) getLayoutInflater().inflate(R.layout.table_row_for_manag, null);

        TextView tvCategory = row.findViewById(R.id.tvCategor);
        EditText et_actual_hrr = row.findViewById(R.id.et_actual_hr);
        EditText et_actual_prr = row.findViewById(R.id.et_actual_per);

        et_actual_hrr.setHint(target_hr);
        et_actual_prr.setHint(target_per);

        if (!edit_view) {
            et_actual_hrr.setEnabled(edit_view);
            et_actual_prr.setEnabled(edit_view);
        } else {
            et_actual_hrr.setEnabled(true);
            et_actual_prr.setEnabled(true);
        }

        TextView tvLastWeek = row.findViewById(R.id.tvushour);
        TextView tvCumulative = row.findViewById(R.id.tvusper);
        TextView tvpointsLastWeek = row.findViewById(R.id.tvvariance);


        tvCategory.setText(categoryName);

        tvLastWeek.setText(lastWeek);
        tvCumulative.setText(cumulative);
        tvpointsLastWeek.setText(pointslastWe);


        et_actual_hrr.setTag(usageid + "_actual_hr");
        et_actual_prr.setTag(usageid + "_actual_per");
        if (!edit_view) {
            // Disable EditText views if edit_view is false
            et_actual_hrr.setEnabled(false);
            et_actual_prr.setEnabled(false);
        } else {
            // Enable EditText views if edit_view is true
            et_actual_hrr.setEnabled(true);
            et_actual_prr.setEnabled(true);
        }

        et_actual_hrr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Calculate hour in terms of percent when focus is lost from et_actual_hrr
                    String hrValue = et_actual_hrr.getText().toString();
                    if (!hrValue.isEmpty()) {
                        double hour = Double.parseDouble(hrValue);

                        double percent = hour * 100 / 168;
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        String percentFormatted = decimalFormat.format(percent);
                        et_actual_prr.setText(String.valueOf(percentFormatted));
                    }
                }
            }
        });
        et_actual_prr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Calculate percent in terms of hours when focus is lost from et_actual_prr
                    String percentValue = et_actual_prr.getText().toString();
                    if (!percentValue.isEmpty()) {
                        double percent = Double.parseDouble(percentValue);
                        // Perform the calculation to get hours
                        double hours = percent * 168 / 100;
                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        String hoursFormatted = decimalFormat.format(hours);
                        et_actual_hrr.setText(String.valueOf(hoursFormatted));
                    }
                }
            }
        });

        etList.add(et_actual_hrr);

        etList.add(et_actual_prr);


        tableLayout.addView(row);
    }

    public void addData(JSONObject jsonbdy) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        String uid = servic.datasender(jsonbdy, "/usageStats/setWeeklyTarget", firebaseToken);
        Log.d("setWeeklyTarget","setWeeklyTarget : " +uid);


    }

    private class FloatingViewTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // Record initial position and touch coordinates
                    initialX = (int) event.getRawX();
                    initialY = (int) event.getRawY();
                    initialTouchX = event.getX();
                    initialTouchY = event.getY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    // Calculate the new position based on touch movement
                    int deltaX = (int) (event.getRawX() - initialX);
                    int deltaY = (int) (event.getRawY() - initialY);

                    // Calculate the new position of the floating view within the container
                    int newX = (int) (initialX + deltaX - initialTouchX);
                    int newY = (int) (initialY + deltaY - initialTouchY);

                    // Set the new position of the floating view
                    floatingView.setX(newX);
                    floatingView.setY(newY);
                    return true;
                default:
                    return false;
            }
        }
    }
}