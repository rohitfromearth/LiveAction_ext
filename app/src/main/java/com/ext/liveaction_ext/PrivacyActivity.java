package com.ext.liveaction_ext;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyActivity extends AppCompatActivity {
    WebView webView, webView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_activity);

        getSupportActionBar().setTitle("Life Actions");

       /* WebSettings webSetting = webView.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient());*/
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/privacypolicy.html");


    }
}
