package com.ext.liveaction_ext.Screens;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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
    ArrayList<Double> valuesperList = new ArrayList<>();
    ArrayList<Double> valueshrList = new ArrayList<>();// Create an ArrayList to store the values
    JSONObject jsonBody = new JSONObject();
    int uid_z;
    int uid = 0;
    String firebaseToken = "";
    boolean target_edi = false;
    String firebaseToke = "";
    JSONArray jsonAy = new JSONArray();
    TextView tv_btn;
    CardView btn_sv, btn_dash;
    double valueperInt, valuehrInt;
    private final ArrayList<EditText> etList = new ArrayList<>();
    private TableLayout tableLayout;
    private FirebaseAuth mAuth;
    String res;
    private boolean isUpdating = false;
    private TableLayout row;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_manage_screen, container, false);


        btn_sv = view.findViewById(R.id.save_btn);
        tv_btn = view.findViewById(R.id.savetargt);
        tableLayout = view.findViewById(R.id.tableLayout);
        // floatingViewContainer = view.findViewById(R.id.floatingViewContainer);
        tv_commnt = view.findViewById(R.id.comment_live_act);
        mAuth = FirebaseAuth.getInstance();
        btn_dash = view.findViewById(R.id.dashboard_btn_mng);
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

        res = servic.pack_rule("/usageStats/manageData?duration=week&userId=" + uid, firebaseToken);

        Log.e("res", "/usageStats/manageData : " + res);
        boolean edit = onPostExecute(res);
        if (edit) {
            tv_btn.setText("Save Target");
        }
        String res_comnt = servic.pack_rule("/usageStats/comments/" + uid, firebaseToken);

        Log.e("res_comnt", "/usageStats/comments/ : " + res_comnt);

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
                    Log.e("value", "value : " + value);

                    String editTextValuehr = editTextforhr.getText().toString();
                    Log.e("editTextValuehr", "editTextValuehr : " + editTextValuehr);

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
                            valuehrInt = Double.parseDouble(editTextValuehr.trim());
                            valueshrList.add(valuehrInt);

                        } else {
                            financeObj.put("targetInPercent", editTextValuehr);
                            valueperInt = Double.parseDouble(editTextValuehr.trim());
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
                boolean validatehr = validationonhr(valueshrList);
                if (validateper && validatehr) {
                    try {
                        addData(jsonBody);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    valuesperList.clear();
                    valueshrList.clear();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    builder.setTitle("Validation Error");
                    builder.setMessage("Total of target in hour as well " + " in percentage should be 100 or less than 100");

                    builder.setPositiveButton("OK", (dialog, which) -> {

                    });

                    // Create and show the alert dialog
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

tableLayout.removeAllViews();


                String re = servic.pack_rule("/usageStats/manageData?duration=week&userId=" + uid, firebaseToken);

                Log.e("res", "/usageStats/manageData : " + re);
                boolean ed = onPostExecute(re);
                if (ed) {
                    tv_btn.setText("Save Target");
                }
                String res_c = servic.pack_rule("/usageStats/comments/" + uid, firebaseToken);

                Log.e("res_comnt", "/usageStats/comments/ : " + res_c);

                try {
                    JSONObject jsonObj = new JSONObject(res_c);
                    String Comments_live_action = jsonObj.getString("result");
                    tv_commnt.setText(Comments_live_action);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }




            }
        });



        btn_dash.setOnClickListener(v -> startActivity(new Intent(getContext(), Dashboard.class)));
        return view;
    }

    private boolean onPostExecute(String result) {
        Log.e("onPostExecute","onPostExecute");
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
                        target_per = " ";
                    } else if (targetinmin.equals("0") && !targetinper.equals("0")) {
                        target_hr = "";
                        target_per = targetinper + "%";
                    } else if (!targetinmin.equals("0") && targetinper.equals("0")) {
                        target_hr = targetinmin;
                        target_per = " ";
                    } else {
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

    private boolean validationonper(ArrayList<Double> valuesList) {
        int sum = 0;
        for (double number : valuesList) {
            sum += number;
        }
        return sum <= 100;
    }

    private boolean validationonhr(ArrayList<Double> valuesList) {
        int sum = 0;
        for (double number : valuesList) {
            sum += number;
        }
        return sum <= 168;
    }

    private void addTableRow(int usageid, String target_hr, String target_per, String categoryName, String lastWeek, String cumulative, String pointslastWe, boolean edit_view) {
        Log.e("addTableRow","addTableRow");

        row = (TableLayout) getLayoutInflater().inflate(R.layout.table_row_for_manag, null);

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
        TextWatcher hourWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isUpdating) {
                    return;
                }
                isUpdating = true;
                String hrValue = et_actual_hrr.getText().toString();
                if (hrValue.isEmpty()) {
                    et_actual_prr.setText("");
                } else {
                    try {
                        double hour = Double.parseDouble(hrValue);
                        if (hour > 168) {
                            et_actual_hrr.setError("Hour value cannot exceed 168");
                            et_actual_prr.setText("");
                        } else {
                            double percent = hour * 100 / 168;
                            DecimalFormat decimalFormat = new DecimalFormat("#.##");
                            String percentFormatted = decimalFormat.format(percent);
                            et_actual_prr.setText(percentFormatted);
                        }
                    } catch (NumberFormatException e) {
                        et_actual_prr.setText("");

                    }
                }
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        TextWatcher percentWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isUpdating) {
                    return;
                }
                isUpdating = true;
                String percentValue = et_actual_prr.getText().toString();
                if (percentValue.isEmpty()) {
                    et_actual_hrr.setText("");
                } else {
                    try {
                        double percent = Double.parseDouble(percentValue);
                        if (percent > 100) {
                            et_actual_prr.setError("Percent value cannot exceed 100");
                            et_actual_hrr.setText("");
                        } else {
                            double hours = percent * 168 / 100;
                            DecimalFormat decimalFormat = new DecimalFormat("#.##");
                            String hoursFormatted = decimalFormat.format(hours);
                            et_actual_hrr.setText(hoursFormatted);
                        }
                    } catch (NumberFormatException e) {
                        et_actual_hrr.setText("");
                    }
                }
                isUpdating = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        et_actual_hrr.addTextChangedListener(hourWatcher);
        et_actual_prr.addTextChangedListener(percentWatcher);

        et_actual_hrr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String hrValue = et_actual_hrr.getText().toString();
                    if (!hrValue.isEmpty()) {
                        try {
                            double hour = Double.parseDouble(hrValue);
                            if (hour <= 168) {
                                double percent = hour * 100 / 168;
                                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                String percentFormatted = decimalFormat.format(percent);
                                et_actual_prr.setText(percentFormatted);
                            }
                        } catch (NumberFormatException e) {
                            et_actual_prr.setText("");
                        }
                    }
                }
            }
        });

        et_actual_prr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String percentValue = et_actual_prr.getText().toString();
                    if (!percentValue.isEmpty()) {
                        try {
                            double percent = Double.parseDouble(percentValue);
                            if (percent <= 100) {
                                double hours = percent * 168 / 100;
                                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                String hoursFormatted = decimalFormat.format(hours);
                                et_actual_hrr.setText(hoursFormatted);
                            }
                        } catch (NumberFormatException e) {
                            et_actual_hrr.setText("");
                        }
                    }
                }
            }
        });


        etList.add(et_actual_hrr);

        etList.add(et_actual_prr);

        tableLayout.addView(row);
    }

    public void addData(JSONObject jsonbdy) throws JSONException {
        Log.e("addData","addData");

      //  boolean success;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String uid = servic.datasender(jsonbdy, "/usageStats/setWeeklyTarget", firebaseToken);
        Log.d("setWeeklyTarget", "setWeeklyTarget : " + uid);

    }

}





///////////////////////////

