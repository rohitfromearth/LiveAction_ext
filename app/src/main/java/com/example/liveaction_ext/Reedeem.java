package com.example.liveaction_ext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Reedeem extends AppCompatActivity {
    Conn_service conn=new Conn_service();

    String firebaseToken="";
    CardView reed_btn,dash_bt,achiv_bt;
    ImageButton close_dial;
    private TableLayout tableLayout;

    TextView total_pnt, pnt_redmed,pnt_avalble,count_txt, txt_hstry,achi_points;
    int counter= 00;
    int uid_z;
    int uid=0;
    CardView plus_count,minus_count;
    private FirebaseAuth mAuth;
    Dialog dialog;
    int pointsAvailable;
    String firebaseToke= "";
    int totalPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reedeem);
        total_pnt=findViewById(R.id.tv_Total_pnt);
        pnt_redmed= findViewById(R.id.tv_pnt_redmed);
        pnt_avalble=findViewById(R.id.tv_pont_avalbl);
        count_txt = findViewById(R.id.tv_count);
        dash_bt=findViewById(R.id.dashbord_btn);
        achiv_bt=findViewById(R.id.achev_btn);
        minus_count = findViewById(R.id.tv_minus_count);
        plus_count = findViewById(R.id.tv_plus_count);
        achi_points= findViewById(R.id.achi_points);
        reed_btn= findViewById(R.id.Reedem_btn);
        txt_hstry=findViewById(R.id.red_histry);

        dialog = new Dialog(Reedeem.this);
        dialog.setContentView(R.layout.dialog_for_reedeem);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        close_dial= dialog.findViewById(R.id.close_permission);

        tableLayout =dialog.findViewById(R.id.tableLayout);
        mAuth = FirebaseAuth.getInstance();
        Log.e("mAuth3", String.valueOf(mAuth));
        FirebaseUser use= mAuth.getCurrentUser();
        Log.e("mAuth4", String.valueOf(use));
 if (use != null) {
            use.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                firebaseToke = task.getResult().getToken();
                                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                myEdit.putString("firebaseToken", firebaseToke);
                                myEdit.apply();
                            }


                        }

                    });

        }
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        uid = sh.getInt("UID", uid_z);
    firebaseToken= sh.getString("firebaseToken","");

        dash_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Reedeem.this,Chart_page.class));
            }
        });
        achiv_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Reedeem.this,Achieve.class));
            }
        });
        String ress = conn.pack_rule("/usageStats/getPoints/"+uid, firebaseToken);

        try {
            JSONObject jsonResponse = new JSONObject(ress);
            JSONObject resultObject = jsonResponse.getJSONObject("result");

            totalPoints = resultObject.getInt("total_points");
            int pointsRedeemed = resultObject.getInt("points_redeemed");
            pointsAvailable = resultObject.getInt("points_available");
            total_pnt.setText(String.valueOf(totalPoints));
            achi_points.setText(String.valueOf(totalPoints));
            pnt_redmed.setText(String.valueOf(pointsRedeemed));
            pnt_avalble.setText(String.valueOf(pointsAvailable));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        close_dial.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                int countOf_row = tableLayout.getChildCount()-1;
                tableLayout.removeViews(1, countOf_row);
            }
        });
        txt_hstry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);
                String res = conn.pack_rule("/usageStats/redemptionHistory/"+uid,firebaseToken);

                onPostExecute(res);

            }
        });
        minus_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pp =  decreaseCounter();
                String before_msg = "Sorry!!! You have already get";
                String after_msg = "voucher according to the available points. Thank you";

                int limit_point = pp*30;
                if(limit_point < pointsAvailable){
                    Log.e("tag---","match");
                    reed_btn.setEnabled(true);
                    reed_btn.setCardBackgroundColor(Color.parseColor("#fe7e34"));
                }
                else{
                    reed_btn.setEnabled(false);
                    reed_btn.setCardBackgroundColor(Color.parseColor("#808080"));
                    AlertDialog.Builder builder = new AlertDialog.Builder(Reedeem.this);

                    // Set the title and message for the dialog
                    builder.setTitle("Incorrect Reedeemtion:");
                    builder.setMessage("sorry you are not allowed for more vouchers ");
                    //builder.setMessage("Thank You");


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
        });
        plus_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int pp =  increaseCounter();
                String before_msg = "Sorry!!! You have already get";
                String after_msg = "voucher according to the available points. Thank you";

                int limit_point = pp*30;
                if(limit_point < pointsAvailable){
                    Log.e("tag---","match");
                    reed_btn.setEnabled(true);
                }
                else{
                    reed_btn.setEnabled(false);
                    reed_btn.setCardBackgroundColor(Color.parseColor("#808080"));
                    AlertDialog.Builder builder = new AlertDialog.Builder(Reedeem.this);

                    // Set the title and message for the dialog
                    builder.setTitle("Incorrect Redemtion:");
                    builder.setMessage("sorry you are not allowed for more vouchers " );
                    //builder.setMessage("Thank You");


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
        });
        reed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag--=","inside btn reedeem");
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);

                try {


                    JSONObject jsonBody = new JSONObject();



                    jsonBody.put("userId", uid);
                    jsonBody.put("count",  counter);
                    String uid= conn.datasender(jsonBody,"/usageStats/redemption",firebaseToken);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }
    private int decreaseCounter() {
        if (counter > 0) {

            counter--;
            count_txt.setText(String.valueOf(counter));

        }
        return counter;
    }

    private int increaseCounter() {
        counter++;
        count_txt.setText(String.valueOf(counter));
        return counter;
    }
    private void onPostExecute(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("result");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    int value1 = item.getInt("no_of_vouchers");
                    int value2 = item.getInt("total_points_redeemed");
                    Log.e("denodata", String.valueOf(value2));
                    String value1_s= String.valueOf(value1);
                    String value2_s= String.valueOf(value2);
                    String value3 = item.getString("redemption_date");
                    int indexOfT = value3.indexOf('T');



                    String value3_s =value3.substring(0, indexOfT);



                    addTableRow(value1_s, value2_s,value3_s);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
    private void addTableRow(String vhr,String amnt, String Rdate) {
//        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.table_row_item, tableLayout, false);

        TableLayout row = (TableLayout) getLayoutInflater().inflate(R.layout.table_row_reedem_hstry_item, null);

        TextView tvCategory = row.findViewById(R.id.tvVochr_count);
        TextView tvLastWeek = row.findViewById(R.id.tvvouchr_amnt);
        TextView tvCumulative = row.findViewById(R.id.tvRedeem_date);


        tvCategory.setText(vhr);
        tvLastWeek.setText(amnt);
        tvCumulative.setText(Rdate);

        tableLayout.addView(row);


    }

}