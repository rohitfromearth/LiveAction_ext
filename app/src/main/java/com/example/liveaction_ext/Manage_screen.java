package com.example.liveaction_ext;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Manage_screen extends AppCompatActivity {
    Conn_service servic= new Conn_service();
    TextView tv_commnt;
    ArrayList<Integer> valuesList = new ArrayList<>(); // Create an ArrayList to store the values

    private ArrayList<EditText> etListforhr = new ArrayList<>();
    JSONObject jsonBody = new JSONObject();


    int  uid_z;
    int uid =  0;
    String firebaseToken="";
    boolean target_edi=false;


    private LinearLayout floatingViewContainer;
    private View floatingView;
    boolean edit = false ;
    private TableLayout tableLayout;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    JSONArray jsonAy = new JSONArray();
    private float initialTouchY;
    TextView tv_btn;
  CardView btn_sv;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_screen);
btn_sv=findViewById(R.id.save_btn);
tv_btn=findViewById(R.id.savetargt);
        tableLayout = findViewById(R.id.tableLayout);
        floatingViewContainer = findViewById(R.id.floatingViewContainer);
        tv_commnt=findViewById(R.id.comment_live_act);
       SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
       uid = sh.getInt("UID", uid_z);
firebaseToken= sh.getString("firebaseToken","");
Log.e("yghdf",firebaseToken);

        // Inflate the floating view layout
        LayoutInflater inflater = LayoutInflater.from(this);
        floatingView = inflater.inflate(R.layout.layout_bell, floatingViewContainer, false);

        // Add touch listener to make the view movable
        floatingView.setOnTouchListener(new FloatingViewTouchListener());

        // Add the floating view to the container
        floatingViewContainer.addView(floatingView);
//
      String res=servic.pack_rule("/usageStats/getUsageData?duration=week&userId="+uid, firebaseToken);
      boolean edit=  onPostExecute(res);
      Log.e("stringh", String.valueOf(edit));
        if (edit){
            tv_btn.setText("Save Target");
        }
       String res_comnt=servic.pack_rule("/usageStats/comments/"+uid, firebaseToken);


       try {
           JSONObject jsonObj = new JSONObject(res_comnt);
           String Comments_live_action= jsonObj.getString("result");
           tv_commnt.setText(Comments_live_action);

       } catch (JSONException e) {
           throw new RuntimeException(e);
       }



btn_sv.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {


if(edit){
        for (EditText editTextforhr : etListforhr) {
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
                if (value.contains("_target_hr")) {
                    financeObj.put("targetInMins", editTextValuehr);

                } else {
                    financeObj.put("targetInPercent", editTextValuehr);
                    int valueInt = Integer.parseInt(editTextValuehr.trim());
                    valuesList.add(valueInt);
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
        Log.e("jsonbody", String.valueOf(jsonBody));
        boolean validate = validationonper(valuesList);
        if (validate) {
            addData(jsonBody);
            startActivity(new Intent(Manage_screen.this, Achieve.class));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Manage_screen.this);

            // Set the title and message for the dialog
            builder.setTitle("Validation Error");
            builder.setMessage("Total of target in percentage should be 100 or less than 100");

            // Set a positive button and its click listener
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Handle the click event (if needed)
                }
            });

            // Create and show the alert dialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


    }
    else{
    startActivity(new Intent(Manage_screen.this, Achieve.class));
}}
});



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

    private boolean onPostExecute(String result) {
        boolean editable = false;
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("result");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String categoryName = item.getString("category");
                    int usage_id = item.getInt("usacm_id");
                    String lastWeek = item.getString("usage_in_mins");
                    String cumulative = item.getString("usage_percent");
                    String pointslastweek = item.getString("target_variance");
                    target_edi= item.getBoolean("target_editable");
//                        boolean target_edi= true;
                    addTableRow(usage_id,categoryName, lastWeek, cumulative,pointslastweek,target_edi);
                    if(usage_id==1) {
                        editable = target_edi;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return editable;
    }
    private boolean validationonper(ArrayList<Integer> valuesList){
        int sum = 0;
        for (int number : valuesList) {
            sum += number;
        }

// Check if the sum is 100 or less
        if (sum <= 100) {
 return true;
        } else {
        return false;
        }
    }
    private void addTableRow(int usageid, String categoryName, String lastWeek, String cumulative, String pointslastWe, boolean edit_view) {
//        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.table_row_item, tableLayout, false);
Log.e("dataloop", String.valueOf(edit_view));
     TableLayout row = (TableLayout) getLayoutInflater().inflate(R.layout.table_row_for_manag, null);

        TextView tvCategory = row.findViewById(R.id.tvCategor);
        EditText et_actual_hrr= row.findViewById(R.id.et_actual_hr);
        EditText et_actual_prr=row.findViewById(R.id.et_actual_per);
        if (!edit_view){
           et_actual_hrr.setEnabled(edit_view);
            et_actual_prr.setEnabled(edit_view);
        }
        else{
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


        etListforhr.add(et_actual_hrr);

       etListforhr.add(et_actual_prr);


        tableLayout.addView(row);
    }
    public void addData(JSONObject jsonbdy){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
Log.e("stron",firebaseToken);

        String uid= servic.datasender(jsonbdy,"/usageStats/setWeeklyTarget",firebaseToken);


    }
    }