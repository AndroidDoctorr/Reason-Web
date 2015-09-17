package com.elevenfifty.reasonweb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by Andrew on 9/17/2015.
 *
 */

public class WebviewActivity extends Activity {
    private WebView webView;

    Intent intent = getIntent();
    String text = intent.getStringExtra("url");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.loadUrl(text);

    }
}