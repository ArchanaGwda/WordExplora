package com.dsa.word.explora.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dsa.word.explora.R;
import com.dsa.word.explora.utils.Utils;

public class WebViewActivity extends Activity implements AppConstants {

    WebView webView;
    ProgressBar progressBar;
    private String websiteUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        TextView title = findViewById(R.id.toolbar_title);
        ImageButton back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressBar = findViewById(R.id.progress_spinner);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorWhite),
                android.graphics.PorterDuff.Mode.SRC_IN);

        Bundle bundle = getIntent().getExtras();
        websiteUrl = bundle.getString(AppConstants.URL);
        title.setText(bundle.getString(AppConstants.TITLE));

        webView = findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress * 100);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), R.string.error_loading_page, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        if (Utils.isInternetAvailable(WebViewActivity.this)) {
            webView.loadUrl(websiteUrl);
        } else {
            Toast.makeText(this, R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Calling super.onBackPressed() to go back to Navigator Activity
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            // Go back to old url
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
