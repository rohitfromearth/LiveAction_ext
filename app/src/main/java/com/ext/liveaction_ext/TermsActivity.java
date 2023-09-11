package com.ext.liveaction_ext;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class TermsActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_activity);
        //setContentView(R.layout.privacy_policy);


        getSupportActionBar().setTitle("Life Actions");


        webView = findViewById(R.id.webView1);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/termscondition.html");


    }
}

