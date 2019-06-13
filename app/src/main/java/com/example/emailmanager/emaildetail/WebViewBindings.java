package com.example.emailmanager.emaildetail;

import android.webkit.WebView;

import androidx.databinding.BindingAdapter;

import com.example.xrwebviewlibrary.XRWebView;

public class WebViewBindings {

    @BindingAdapter("android:html")
    public static void setHtml(WebView webView, String html) {
        XRWebView.with(webView).simple().build().loadHtml(html, "text/html", "utf-8");
    }
}
