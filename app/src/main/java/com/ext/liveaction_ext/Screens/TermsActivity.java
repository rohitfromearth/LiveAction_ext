package com.ext.liveaction_ext.Screens;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.ext.liveaction_ext.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TermsActivity extends AppCompatActivity {
    WebView webView;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser use = mAuth.getCurrentUser();

        if (use == null ) {
            Intent intent = new Intent(this, Login_verifcation.class);
            startActivity(intent);
            finish();
        }
else {
            setContentView(R.layout.terms_activity);
        }



        getSupportActionBar().setTitle(R.string.app_name);

        webView = findViewById(R.id.webView1);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/termscondition.html");


    }
}

