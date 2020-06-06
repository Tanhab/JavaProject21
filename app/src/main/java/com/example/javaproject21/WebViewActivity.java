package com.example.javaproject21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

/**
 * The class for Web view activity.
 */
public class WebViewActivity extends AppCompatActivity {
    /**
     * The constant variable for logcat.
     */
    private static final String TAG = "WebViewActivity";
    /**
     * The WebView variable.
     */
    private WebView webView;
    /**
     * ImageButton for back.
     */
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView=findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        btnBack=findViewById(R.id.btnBack);
        Intent intent= getIntent();
        String Url= intent.getStringExtra("Url");
        Log.d(TAG, "onCreate: url "+ Url);
        webView.loadUrl(Url);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
