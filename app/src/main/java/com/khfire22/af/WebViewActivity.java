package com.khfire22.af;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Kevin on 2/2/2016.
 */
public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private String position;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        position = intent.getStringExtra("position");

//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mWebView = new WebView(this);

        if (position.equals("0")) {
            mWebView.loadUrl("https://m.abercrombie.com");
        } else {
            mWebView.loadUrl("https://m.hollisterco.com");
        }
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        this.setContentView(mWebView);




    }

   public void openWebView(int position) {

        if (position == 0) {
//            webView.loadUrl("https://m.abercrombie.com");
        } else {
//            webView.loadUrl("https://m.hollisterco.com");
        }
    }




}
