package com.ext.liveaction_ext.Screens;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
import com.ext.liveaction_ext.frags.DayFragment;
import com.ext.liveaction_ext.service_clas.Conn_service;
import com.ext.liveaction_int.Access_new;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Dashboard extends AppCompatActivity implements Conn_service.PackRuleCallback {

    TextView tvP_usernme, privacyTet, termsTet, sync_tym;
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
    String res;
    FragmentManager fragmentManager;
    Context context;

    Calendar today;
    ArrayList<String> appl = new ArrayList<String>();
    ArrayList<String> dummyList = new ArrayList<String>();
    ArrayList<String> dayList = new ArrayList<String>();
    ArrayList<String> weekList = new ArrayList<String>();
    ArrayList<String> monthList = new ArrayList<String>();
    //    ArrayList<String> yearList = new ArrayList<String>();
//    SharedPreferences sh;
    DayFragment df = new DayFragment();
    boolean showDummy;
    public String firebaseToke = "";
    String userid = "";
    ProgressBar pro;
    SharedPreferences sh;

    private boolean isLoggedIn = false;
    String timeStatus = "Last synced at";
    String data_sync;
    FirebaseAuth mAuth;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                4);
        sh = getApplicationContext().getSharedPreferences("LifeSharedPref", MODE_PRIVATE);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser use = mAuth.getCurrentUser();

        pro = findViewById(R.id.progressBr);


        pro.setVisibility(View.VISIBLE);


        if (use != null) {
            use.getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            firebaseToke = task.getResult().getToken();
                            Uioperation(firebaseToke);
                        }
                    });
        } else {

            Intent intent = new Intent(Dashboard.this, Login_verifcation.class);
            startActivity(intent);
            finish();
        }


        uid = sh.getInt("UID", uid_z);
        username = sh.getString("username", "");
        mobile = sh.getString("mobile_number", "");
        Set<String> set = sh.getStringSet("APP_LIST", null);
        appl.addAll(set);
        userid = "user id : " + uid;

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
                accessibility_permission.setEnabled(usage_stat.isEnabled());
            });
            accessibility_permission.setOnClickListener(view -> {
                view.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view.animate().alpha(1f).setDuration(200)).start();
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            });

        }
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        manag_btn = findViewById(R.id.mange_btn);
        achieve_btn = findViewById(R.id.achie_btn);
        connect_btn = findViewById(R.id.connectbtn);
        sync_tym = findViewById(R.id.tym_sync);
        faq_btn = findViewById(R.id.FAQ_btn);
        termsTet = findViewById(R.id.termsd);
        privacyTet = findViewById(R.id.privacyd);
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
        phone.setText(userid);

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
        privacyTet.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), PrivacyActivity.class)));
        termsTet.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), TermsActivity.class)));
        tvP_usernme = findViewById(R.id.usrname);
        tvP_usernme.setText(username);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        pro.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {

    }

    private void Uioperation(String firebaseTok) {

        long lastTaskDate = sh.getLong("KEY_LAST_TASK_DATE", 0); // 0 means thed task has never been performed

        today = Calendar.getInstance();
        Calendar lastTaskCalendar = Calendar.getInstance();
        lastTaskCalendar.setTimeInMillis(lastTaskDate);

        if (today.get(Calendar.YEAR) == lastTaskCalendar.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == lastTaskCalendar.get(Calendar.DAY_OF_YEAR)) {
            // Task has already been performed today
        } else {
            // Task has not been performed today

            Conn_service conn = new Conn_service(this);
            conn.pack_rule("/usageStats/getUsageData?userId=" + uid, firebaseTok, this);

//                    conn.pack_rule("/usageStats/getUsageData?userId=" + uid, firebaseTok);

        }

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
                Log.e("Week", String.valueOf(weekList));
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        data_sync = sh.getString("Last_sync", "");

        if (data_sync.isEmpty()) {
            // If no data is available, display a dash "-"
            sync_tym.setText(timeStatus + "-");
        } else {
            // If data is available, display the value retrieved from shared preferences
            sync_tym.setText(timeStatus + ": " + data_sync);
        }
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
            mAuth.signOut();
            Intent intent = new Intent(this, Login_verifcation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            getSupportActionBar().setTitle("Verification");
        }
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    public void onResult(String result) {
        SharedPreferences.Editor myEdit = sh.edit();
        String dir = getObbDir().getPath();
        dayList = dataDayList(result);
        weekList = dataWeekList(result);
        monthList = dataMonthList(result);
        myEdit.putString("dir", dir);
        Set<String> dayset = new HashSet<String>();
        dayset.addAll(dayList);
        Set<String> weekset = new HashSet<String>();
        weekset.addAll(weekList);
        Set<String> monthset = new HashSet<String>();
        monthset.addAll(monthList);
        myEdit.putStringSet("DAY_LIST", dayset);
        myEdit.putStringSet("WEEK_LIST", weekset);
        myEdit.putStringSet("MONTH_LIST", monthset);


        myEdit.putLong("KEY_LAST_TASK_DATE", today.getTimeInMillis());
        myEdit.putBoolean("Dashboard", true);
        myEdit.apply();

        df.Data_Show();
    }
//    private void initPopupDialog(Context context) {
//        // Create and initialize the popup dialog
//        Dialog dd = new Dialog();
//
//        dd.setContentView(R.layout.navigation_permission_dialog);
//        dd.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dd.setCancelable(false);
//        dd.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        dd.show();
//        usage_stat = dd.findViewById(R.id.usaegstat_perm1);
//        accessibility_permission = dd.findViewById(R.id.accessibilty_perm1);
//        close_dialog = dd.findViewById(R.id.close_permission1);
//        close_dialog.setOnClickListener(view -> {
//            view.animate().alpha(0.5f).setDuration(200).withEndAction(() -> view.animate().alpha(1f).setDuration(200)).start();
//            dd.dismiss();
//        });
//    }
//
//    private void showPopupDialog() {
//        // Show the initialized popup dialog
//        if (!dd.isShowing()) {
//            dd.show();
//        }
//    }

}