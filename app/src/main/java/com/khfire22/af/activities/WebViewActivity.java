package com.khfire22.af.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Kevin on 2/2/2016.
 */
public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "WebViewActivity";
    private WebView mWebView;
    private String target;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get intent data from sending activity
        Intent intent = getIntent();
        target = intent.getStringExtra("position");
        mWebView = new WebView(this);

        // Open the correct browser based on the positon that was clicked and passed
        switch (target) {
            case "0":
                mWebView.loadUrl("https://m.abercrombie.com");
                break;

            case "1":
                mWebView.loadUrl("https://m.hollisterco.com");
                break;

            default:
                mWebView.loadUrl("https://www.abercrombie.com/anf/media/legalText/viewDetailsText20150618_Shorts25_US.html");
                break;
        }

        // Open up the specified URL in the webView
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Log.d(TAG, "URL = " + url);
                return true;
            }
        });

        this.setContentView(mWebView);
    }
}
