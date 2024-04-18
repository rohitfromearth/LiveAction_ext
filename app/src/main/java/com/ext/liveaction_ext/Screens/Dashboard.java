package com.ext.liveaction_ext.Screens;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.ext.liveaction_ext.Addaptors.MyPagerAdapter;
import com.ext.liveaction_ext.DummyData;
import com.ext.liveaction_ext.R;
import com.ext.liveaction_ext.service_clas.Conn_service;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Dashboard extends AppCompatActivity {

    TextView tvP_usernme;

    CardView manag_btn, achieve_btn, faq_btn, connect_btn;
    String username;
    int uid_z;
    int uid = 0;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    CardView usage_stat, accessibility_permission;
    ImageButton close_dialog;
    DummyData dumdta = new DummyData();
    String mobile;
    FragmentManager fragmentManager;
    Context context;
    Conn_service conn = new Conn_service();
    ArrayList<String> dummyList = new ArrayList<String>();
    ArrayList<String> dayList = new ArrayList<String>();
    ArrayList<String> weekList = new ArrayList<String>();
    ArrayList<String> monthList = new ArrayList<String>();
    ArrayList<String> yearList = new ArrayList<String>();
    SharedPreferences sh;
    boolean showDummy;
    private boolean isLoggedIn = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                4);
        //FirebaseApp.initializeApp(this);

        sh = getApplicationContext().getSharedPreferences("LifeSharedPref",MODE_PRIVATE);
        isLoggedIn = sh.getBoolean("isLoggedIn", false);
        uid = sh.getInt("UID", uid_z);
        username = sh.getString("username", "");
        mobile = sh.getString("mobile_number", "");



        String dir = getObbDir().getPath();

        setContentView(R.layout.drawer_layout);

        if (getIntent().getBooleanExtra("show_permission_dialog", false)) {

            Dialog dd = new Dialog(Dashboard.this);

            dd.setContentView(R.layout.navigation_permission_dialog);
            dd.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dd.setCancelable(false);
            dd.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dd.show();
            usage_stat = dd.findViewById(R.id.usaegstat_perm1);
            accessibility_permission = dd.findViewById(R.id.accessibilty_perm1);
            close_dialog = dd.findViewById(R.id.close_permission1);

            close_dialog.setOnClickListener(view -> {
                view.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view.animate().alpha(1f).setDuration(200)).start();
                dd.dismiss();
            });
            usage_stat.setOnClickListener(view -> {
                view.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view.animate().alpha(1f).setDuration(200)).start();
                startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

                // Disable button2
                accessibility_permission.setEnabled(usage_stat.isEnabled());  // Enable button2
            });
            accessibility_permission.setOnClickListener(view -> {

                view.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view.animate().alpha(1f).setDuration(200)).start();
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            });

        }
//        }



        String fToken = sh.getString("firebaseToken", "");//april


        String resultdummy = dumdta.Data_for_dash();
        String res = conn.pack_rule("/usageStats/getUsageData?userId=" + uid, fToken);


        try {
            JSONObject jsonObject = new JSONObject(res);
            showDummy = jsonObject.getBoolean("showDummy");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



        dummyList = dummyDayList(resultdummy);
        dayList = dataDayList(res);
        weekList = dataWeekList(res);
        monthList = dataMonthList(res);
        yearList = dataYearList(res);

        sh = getApplicationContext().getSharedPreferences("LifeSharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();

        myEdit.putString("dir", dir);
        myEdit.putBoolean("showDummy", showDummy);



        Set<String> dummyset = new HashSet<String>();
        dummyset.addAll(dummyList);

        Set<String> dayset = new HashSet<String>();
        dayset.addAll(dayList);

        Set<String> weekset = new HashSet<String>();
        weekset.addAll(weekList);

        Set<String> monthset = new HashSet<String>();
        monthset.addAll(monthList);

        Set<String> yearset = new HashSet<String>();
        yearset.addAll(yearList);


        myEdit.putStringSet("DUMMY_LIST", dummyset);
        myEdit.putStringSet("DAY_LIST", dayset);
        myEdit.putStringSet("WEEK_LIST", weekset);
        myEdit.putStringSet("MONTH_LIST", monthset);
        myEdit.putStringSet("YEAR_LIST", yearset);


        myEdit.apply();

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        manag_btn = findViewById(R.id.mange_btn);
        achieve_btn = findViewById(R.id.achie_btn);
        connect_btn = findViewById(R.id.connectbtn);
        faq_btn = findViewById(R.id.FAQ_btn);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.nav_open, R.string.nav_close);
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);

        navigationView = (NavigationView) findViewById(R.id.nvView);
        View header = navigationView.getHeaderView(0);
        TextView name = header.findViewById(R.id.user_name);
        TextView phone = header.findViewById(R.id.user_phone);
        name.setText(username);
        phone.setText(mobile);
        setupDrawerContent(navigationView);

        manag_btn.setOnClickListener(view -> {
            view.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view.animate().alpha(1f).setDuration(200)).start();

            Fragment go_next = new Manage_screen();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();
            getSupportActionBar().setTitle("Manage");
        });
        achieve_btn.setOnClickListener(view -> {
            view.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view.animate().alpha(1f).setDuration(200)).start();

            Fragment go_next = new Achieve();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();
            getSupportActionBar().setTitle("Achievements");
        });
        faq_btn.setOnClickListener(view -> {

            view.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view.animate().alpha(1f).setDuration(200)).start();
            //startActivity(new Intent(Dashboard.this, FaQ.class));
            Fragment go_next = new FaQ();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();
            getSupportActionBar().setTitle("FAQ");
        });
        connect_btn.setOnClickListener(view -> {
            view.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view.animate().alpha(1f).setDuration(200)).start();
            Fragment go_next = new ContactUs();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();
            getSupportActionBar().setTitle("Contact us");
        });
        tvP_usernme = findViewById(R.id.usrname);

        tvP_usernme.setText(username);

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }
    public ArrayList dummyDayList(String ss) {
        //april
        try {
            JSONObject jsonObject = new JSONObject(ss);
            JSONArray jsonArray = jsonObject.getJSONArray("day");
            for (int i = 0; i < jsonArray.length(); i++) {
                dummyList.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return dummyList;

        //april
    }
    public ArrayList dataDayList(String ss) {
        //april
        try {
            JSONObject jsonObject = new JSONObject(ss);
            JSONArray jsonArray = jsonObject.getJSONArray("day");
            for (int i = 0; i < jsonArray.length(); i++) {
                dayList.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        return dayList;

        //april
    }
    public ArrayList dataWeekList(String ss) {
        //april

        try {
            JSONObject jsonObject = new JSONObject(ss);
            JSONArray jsonArray = jsonObject.getJSONArray("week");
            for (int i = 0; i < jsonArray.length(); i++) {
                weekList.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        return weekList;

        //april
    }

    public ArrayList dataMonthList(String ss) {
        //april
        try {
            JSONObject jsonObject = new JSONObject(ss);
            JSONArray jsonArray = jsonObject.getJSONArray("month");
            for (int i = 0; i < jsonArray.length(); i++) {
                monthList.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return monthList;

        //april
    }
    public ArrayList dataYearList(String ss) {
        //april
        try {
            JSONObject jsonObject = new JSONObject(ss);
            JSONArray jsonArray = jsonObject.getJSONArray("year");
            for (int i = 0; i < jsonArray.length(); i++) {
                yearList.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return yearList;

        //april
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {

        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment go_next;

        int itemId = menuItem.getItemId();


        if (itemId == R.id.chart_page) {

            startActivity(new Intent(this, Dashboard.class));
            Objects.requireNonNull(getSupportActionBar()).setTitle("Dashboard");

        } else if (itemId == R.id.manage_page) {

            go_next = new Manage_screen();
            getSupportActionBar().setTitle("Manage");
            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();

        } else if (itemId == R.id.achieve_page) {

            go_next = new Achieve();
            getSupportActionBar().setTitle("Achievements");
            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();

        } else if (itemId == R.id.redeem_page) {

            go_next = new Reedeem();
            getSupportActionBar().setTitle("Redeem");
            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();

        } else if (itemId == R.id.contact_us) {

            go_next = new ContactUs();
            getSupportActionBar().setTitle("Contact us");
            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();

        } else if (itemId == R.id.faq) {

            go_next = new FaQ();
            getSupportActionBar().setTitle("FAQ");
            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();

        } else if (itemId == R.id.perm_new) {

            Intent intent = new Intent(this, Dashboard.class);
            intent.putExtra("show_permission_dialog", true);
            startActivity(intent);

        } else if (itemId == R.id.Logout) {
            isLoggedIn = false;
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LifeSharedPref",MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
//            myEdit.putString("firebaseToken", "");
            myEdit.clear();
            myEdit.apply();
            //startActivity(new Intent(this, Login_verifcation.class));



            // Clear the activity stack and start a new activity
            Intent intent = new Intent(this, Login_verifcation.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Finish the current activity
            getSupportActionBar().setTitle("Verification");

            myEdit.putBoolean("isLoggedIn", false);
            myEdit.apply();
        }

        // Insert the fragment by replacing any existing fragment


        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.d("destroyed","dashboard back");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("destroyed","dashboard destroyed");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("stop","dashboard stop");
    }
}