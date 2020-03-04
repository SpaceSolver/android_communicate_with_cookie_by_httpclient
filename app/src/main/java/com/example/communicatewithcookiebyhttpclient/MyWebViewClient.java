package com.example.communicatewithcookiebyhttpclient;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MyWebViewClient extends WebViewClient {
    public MyWebViewClient() {
        super();
    }

    //ページの読み込み開始
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
    }

    //ページの読み込み完了
    @Override
    public void onPageFinished(WebView view, String url) {
    }

    //ページの読み込み失敗
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Toast.makeText(view.getContext(), "エラー", Toast.LENGTH_LONG).show();
    }

}
