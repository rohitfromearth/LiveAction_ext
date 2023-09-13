package com.ext.liveaction_ext;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.ext.liveaction_int.Access_new;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Chart_page extends AppCompatActivity {
    Pack_api pc = new Pack_api();
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
    String mobile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                4);
        FirebaseApp.initializeApp(this);

        SharedPreferences sh = getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
        uid = sh.getInt("UID", uid_z);

        username = sh.getString("username", "");
        mobile = sh.getString("mobile_number", "");

        String endpot = "https://lifeactions.online";

        String dir = getObbDir().getPath();
        ArrayList pack = pc.pack_rule(endpot);
        SharedPreferences sharedPreferences = getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("endpt", endpot);
        myEdit.putString("dir", dir);
        Set<String> set = new HashSet<String>();
        set.addAll(pack);
        myEdit.putStringSet("APP_LIST", set);
        myEdit.apply();

        if (uid == 0) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        } else {

            setContentView(R.layout.drawer_layout);

            if (getIntent().getBooleanExtra("show_permission_dialog", false)) {

                Dialog dd = new Dialog(Chart_page.this);

                dd.setContentView(R.layout.navigation_permission_dialog);
                dd.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dd.setCancelable(false);
                dd.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dd.show();
                usage_stat = dd.findViewById(R.id.usaegstat_perm1);
                accessibility_permission = dd.findViewById(R.id.accessibilty_perm1);
                close_dialog = dd.findViewById(R.id.close_permission1);

                close_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                view.animate().alpha(1f).setDuration(200);
                            }
                        }).start();
                        dd.dismiss();
                    }
                });
                usage_stat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                view.animate().alpha(1f).setDuration(200);
                            }
                        }).start();
                        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

                        // Disable button2
                        accessibility_permission.setEnabled(usage_stat.isEnabled());  // Enable button2
                    }
                });
                accessibility_permission.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                view.animate().alpha(1f).setDuration(200);
                            }
                        }).start();
                        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    }
                });

            }
        }

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
        getSupportActionBar().setTitle("Life Actions");

        navigationView = (NavigationView) findViewById(R.id.nvView);
        View header = navigationView.getHeaderView(0);
        TextView name = header.findViewById(R.id.user_name);
        TextView phone = header.findViewById(R.id.user_phone);
        name.setText(username);
        phone.setText(mobile);
        setupDrawerContent(navigationView);

        manag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().alpha(1f).setDuration(200);
                    }
                }).start();
                //startActivity(new Intent(Chart_page.this, Manage_screen.class));
                Fragment go_next = new Manage_screen();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();
                getSupportActionBar().setTitle("Manage");
            }
        });
        achieve_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().alpha(1f).setDuration(200);
                    }
                }).start();

                Fragment go_next = new Achieve();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();
                getSupportActionBar().setTitle("Achievements");
            }
        });
        faq_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().alpha(1f).setDuration(200);
                    }
                }).start();
                //startActivity(new Intent(Chart_page.this, FaQ.class));
                Fragment go_next = new FaQ();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();
                getSupportActionBar().setTitle("FAQ");
            }
        });
        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.animate().alpha(0.5f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate().alpha(1f).setDuration(200);
                    }
                }).start();
                Fragment go_next = new ContactUs();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();
                getSupportActionBar().setTitle("Connect Us");
            }
        });
        tvP_usernme = findViewById(R.id.usrname);

        tvP_usernme.setText(username);
        // Create and set the adapter for the ViewPager
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Link the TabLayout with the ViewPager
        tabLayout.setupWithViewPager(viewPager);
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
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment go_next = null;

        int itemId = menuItem.getItemId();

        if (itemId == R.id.chart_page) {

            startActivity(new Intent(this, Chart_page.class));
            getSupportActionBar().setTitle("Dashboard");

        } else if (itemId == R.id.manage_page) {

            go_next = new Manage_screen();
            getSupportActionBar().setTitle("Manage");

        } else if (itemId == R.id.achieve_page) {

            go_next = new Achieve();
            getSupportActionBar().setTitle("Achievements");

        } else if (itemId == R.id.redeem_page) {

            go_next = new Reedeem();
            getSupportActionBar().setTitle("Redeem");

        } else if (itemId == R.id.contact_us) {

            go_next = new ContactUs();
            getSupportActionBar().setTitle("Connect Us");

        } else if (itemId == R.id.faq) {

            go_next = new FaQ();
            getSupportActionBar().setTitle("FAQ");

        } else if (itemId == R.id.perm_new) {

            Intent intent = new Intent(this, Chart_page.class);
            intent.putExtra("show_permission_dialog", true);
            startActivity(intent);

        } else if (itemId == R.id.Logout) {

            SharedPreferences sharedPreferences = getSharedPreferences("LifeSharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("firebaseToken", "");
            myEdit.apply();
            startActivity(new Intent(this, Login_verifcation.class));
            getSupportActionBar().setTitle("Verification");
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.framelayout, go_next).commit();
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
    }
}