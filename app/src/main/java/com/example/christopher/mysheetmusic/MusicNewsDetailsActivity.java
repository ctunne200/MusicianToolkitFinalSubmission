package com.example.christopher.mysheetmusic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class MusicNewsDetailsActivity extends AppCompatActivity {
    // Creates a Web View, called in MusicNewsRSSAdapter line 42, allowing users to click
    // any card item and open it's respective link inside the app.
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_news_details);
        webView = (WebView) findViewById(R.id.webview);
        Bundle bundle = getIntent().getExtras();
        webView.loadUrl(bundle.getString("Link"));
    }
}
