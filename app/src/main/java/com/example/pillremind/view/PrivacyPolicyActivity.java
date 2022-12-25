package com.example.pillremind.view;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pillremind.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private WebView webView;
    private LinearLayout llBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        webView = findViewById(R.id.privacy_policy_webview);
        webView.loadUrl("file:///android_asset/privacy_policy.html");
        llBack = findViewById(R.id.privacy_policy_toolbar);
        llBack.setOnClickListener(v -> finish());
    }
}