package com.example.pillremind.view;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pillremind.R;

public class TermsActivity extends AppCompatActivity {

    private WebView webView;
    private LinearLayout llBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        webView = findViewById(R.id.privacy_policy_webview);
        webView.loadUrl("file:///android_asset/terms_conditons.html");
        llBack = findViewById(R.id.privacy_policy_toolbar);
        llBack.setOnClickListener(v -> finish());
    }
}