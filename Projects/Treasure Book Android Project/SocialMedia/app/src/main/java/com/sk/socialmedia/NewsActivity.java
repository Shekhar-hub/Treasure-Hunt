package com.sk.socialmedia;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsActivity extends AppCompatActivity {

    WebView web_news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("News");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF4081")));

        web_news=findViewById(R.id.web_news);
        web_news.getSettings().setSupportMultipleWindows(true);
        web_news.getSettings().setJavaScriptEnabled(true);
        web_news.setWebViewClient(new WebViewClient());
        web_news.loadUrl("https://news.google.com/topstories?hl=en-IN&gl=IN&ceid=IN:en");
    }
    @Override
    public void onBackPressed() {
        if (web_news.canGoBack()) {
            web_news.goBack();
        } else {
            startActivity(new Intent(NewsActivity.this,DashboardActivity.class));
            finish();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(NewsActivity.this,DashboardActivity.class));
        finish();
        return super.onSupportNavigateUp();
    }
}