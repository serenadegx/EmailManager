package com.example.emailmanager.emaildetail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.example.emailmanager.R;
import com.example.emailmanager.data.source.EmailRepository;
import com.example.emailmanager.databinding.ActivityEmailDetailBinding;
import com.example.emailmanager.emaildetail.adapter.AccessoryListAdapter;

public class EmailDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEmailDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_email_detail);
        binding.rvAccessory.setLayoutManager(new LinearLayoutManager(this));
        AccessoryListAdapter listAdapter = new AccessoryListAdapter(this);
        binding.rvAccessory.setAdapter(listAdapter);
        EmailDetailViewModel viewModel = new EmailDetailViewModel(this, new EmailRepository());
        viewModel.setAdapter(listAdapter);
        WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBlockNetworkImage(false);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new WebViewClient());
        binding.flCon.addView(webView, new FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        binding.setViewModel(viewModel);
        viewModel.setWebView(webView);
        viewModel.loadDataById(getIntent().getIntExtra("msgnum", 0));
    }

    public static void start2EmailDetailActivity(Context context, int msgNum) {
        context.startActivity(new Intent(context, EmailDetailActivity.class).putExtra("msgnum", msgNum));
    }
}
