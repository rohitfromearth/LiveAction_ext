package com.ext.liveaction_ext.Screens;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.ext.liveaction_ext.R;

public class PrivacyActivity extends AppCompatActivity {
    WebView webView, webView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_activity);

        getSupportActionBar().setTitle("Sondersort");

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/privacypolicy.html");


    }
}
