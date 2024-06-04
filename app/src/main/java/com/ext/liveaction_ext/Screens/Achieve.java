package com.ext.liveaction_ext.Screens;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ext.liveaction_ext.R;
import com.ext.liveaction_ext.service_clas.Conn_service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class Achieve extends Fragment implements Conn_service.PackRuleCallback{
    CardView rede, congrats_tv,btn_dsh;

    int uid_z;
    int uid = 0;
    Calendar today;

    TextView tv_red_points;
    String firebaseToken = "";
    public String firebaseToke = "";
    private TableLayout tableLayout;
//    private LinearLayout floatingViewContainer;

    private FirebaseAuth mAuth;
    private int initialX;
   public SharedPreferences sh;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_achieve, container, false);

        sh = getContext().getSharedPreferences("LifeSharedPref", MODE_PRIVATE);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser use = mAuth.getCurrentUser();




        if (use != null) {
            use.getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            firebaseToke = task.getResult().getToken();
                             fetchdta(firebaseToke);

                        }
                    });

        }else {
            Intent intent = new Intent(getActivity(), Login_verifcation.class);
            startActivity(intent);
            getActivity().finish();
        }

        rede = view.findViewById(R.id.reddem_btn);
        tableLayout = view.findViewById(R.id.tableLayout);


        uid = sh.getInt("UID", uid_z);


        congrats_tv = view.findViewById(R.id.congrats_tv);
        tv_red_points = view.findViewById(R.id.earn_number);
        btn_dsh=view.findViewById(R.id.dash_btn_ach);

        rede.setOnClickListener(view1 -> {

            Fragment go_next = new Reedeem();
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.setTitle("Redeem");
        });
        btn_dsh.setOnClickListener(v -> startActivity(new Intent(getContext(), Dashboard.class)));
        return view;
    }
public void fetchdta(String firebaseTokk){



    Conn_service servic = new Conn_service(getActivity());
        servic.pack_rule("/usageStats/getAchievements/" + uid, firebaseTokk,this);
//                servic.pack_rule("/usageStats/getAchievements/" + uid, firebaseTokk);
        congrats_tv.setVisibility(View.GONE);



}
    private void onPostExecute(String result) {
        if (result != null) {
            getActivity().runOnUiThread(() -> {
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

                        if (pointslastweekint > 0) {
                            congrats_tv.setVisibility(View.VISIBLE);
                            tv_red_points.setText(pointslastweek);
                        }
                    }
                    addTableRow(categoryName, lastWeek, cumulative, pointslastweek, pointscumm);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            });
        }


    }

    private void addTableRow(String categoryName, String lastWeek, String cumulative, String pointslastWe, String pointscumu) {

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

    @Override
    public void onResult(String result) {



            onPostExecute(result);

    }
}