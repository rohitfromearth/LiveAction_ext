package com.example.liveaction_ext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
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

public class Achieve extends AppCompatActivity {
    CardView rede, congrats_tv;
    Conn_service servic = new Conn_service();
    int uid_z;
    int uid = 0;
    TextView tv_red_points;
    String firebaseToken = "";
    String firebaseToke = "";
    private TableLayout tableLayout;
    private LinearLayout floatingViewContainer;
    private View floatingView;
    private FirebaseAuth mAuth;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve);
        rede = findViewById(R.id.reddem_btn);
        tableLayout = findViewById(R.id.tableLayout);
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

        firebaseToken = sh.getString("firebaseToken", "");

        congrats_tv = findViewById(R.id.congrats_tv);
        tv_red_points = findViewById(R.id.earn_number);
        String res = servic.pack_rule("/usageStats/getAchievements/" + uid, firebaseToken);
        congrats_tv.setVisibility(View.GONE);
        onPostExecute(res);
        floatingViewContainer = findViewById(R.id.floatingViewContainer);

        // Inflate the floating view layout
        LayoutInflater inflater = LayoutInflater.from(this);
        floatingView = inflater.inflate(R.layout.layout_bell, floatingViewContainer, false);

        // Add touch listener to make the view movable
        floatingView.setOnTouchListener(new FloatingViewTouchListener());

        // Add the floating view to the container
        floatingViewContainer.addView(floatingView);


        rede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Achieve.this, Reedeem.class));
            }
        });

    }

    private void onPostExecute(String result) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("result");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    String categoryName = item.getString("category_name");
                    String lastWeek = item.getString("last_week");
                    String cumulative = item.getString("cumulative_till_date");
                    int pointslastweekint = item.getInt("points_last_week");
                    int pointscumm_I = item.getInt("points_cumulative");
                    String pointslastweek = String.valueOf(pointslastweekint);
                    String pointscumm = String.valueOf(pointscumm_I);

                    if (categoryName.equals("Total")) {
                        Log.e("datalegend", pointslastweek);
                        if (pointslastweekint > 0) {
                            congrats_tv.setVisibility(View.VISIBLE);
                            tv_red_points.setText(pointslastweekint);

                        }

                    }

                    addTableRow(categoryName, lastWeek, cumulative, pointslastweek, pointscumm);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private void addTableRow(String categoryName, String lastWeek, String cumulative, String pointslastWe, String pointscumu) {
//        TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.table_row_item, tableLayout, false);

        TableLayout row = (TableLayout) getLayoutInflater().inflate(R.layout.table_row_achive_item, null);

        TextView tvCategory = row.findViewById(R.id.tvCategory);
        TextView tvLastWeek = row.findViewById(R.id.tvLastWeek);
        TextView tvCumulative = row.findViewById(R.id.tvCumulative);
        TextView tvpointsLastWeek = row.findViewById(R.id.tv_pnt_last_week);
        TextView tvpointsCumu = row.findViewById(R.id.tv_pnt_cumu);

        tvCategory.setText(categoryName);
        tvLastWeek.setText(lastWeek);
        tvCumulative.setText(cumulative);
        tvpointsLastWeek.setText(pointslastWe);
        tvpointsCumu.setText(pointscumu);

        tableLayout.addView(row);


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