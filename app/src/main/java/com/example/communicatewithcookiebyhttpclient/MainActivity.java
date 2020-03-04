package com.example.communicatewithcookiebyhttpclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView logView;
    private WebView resultLoadView;
    private RequestQueue requestQueueWithCookie;
    private RequestQueue requestQueueNoneCookie;
    private CookieManager androidCookieManager;
    private java.net.CookieManager javaCookieManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logView = (TextView)findViewById(R.id.LogTextView);
        resultLoadView = (WebView)findViewById(R.id.ResultWebView);

        androidCookieManager = CookieManager.getInstance();
        androidCookieManager.setAcceptCookie(true);
        androidCookieManager.setAcceptThirdPartyCookies(resultLoadView, true);

        javaCookieManager = new java.net.CookieManager();
        CookieHandler.setDefault(javaCookieManager);

        String url = getString(R.string.default_url);
        requestQueueWithCookie = Volley.newRequestQueue(this, generateHurlStack(url));
        requestQueueNoneCookie = Volley.newRequestQueue(this);
    }

    private void log(String s, Boolean with) {
        Log.d(TAG, s );
        if( with ) {
            logView.append(s + "\n");
        }
    }

    private void log(String s){
        log(s, true);
    }

    private HurlStack generateHurlStack(String url){
        return new HurlStack() {
            @Override
            public HttpResponse executeRequest(Request<?> request, Map<String, String> additionalHeaders)
                    throws IOException, AuthFailureError {
                Map newHeaders = new HashMap();
                newHeaders.putAll(additionalHeaders);
                // セッション維持のためcookieとUserAgentを設定する
                // 今はWebView側の通信で保持できたcookieを引っ張り出して使っている
                String url = request.getUrl();
                String webviewCookie = androidCookieManager.getCookie(url);
                log("URL [" + url + "]", false);
                log("specified COOKIE [" + newHeaders.toString() + "]", false);
                log("existed COOKIE [" + webviewCookie + "]", false);
                log("domain? [" + URI.create(url) + "]", false);

                java.net.CookieStore store = javaCookieManager.getCookieStore();
                store.add(URI.create(url), new java.net.HttpCookie("Cookie", webviewCookie));

                HttpResponse response = super.executeRequest(request, newHeaders);

                // レスポンス内のcookieの回収
                List<HttpCookie> cookies = store.getCookies();
                if (cookies.size() > 0) {
                    // あればクッキーを保存
                    // cookies.get(0)とかをどうにかこうにか保存します
                }
                return response;
            }
        };
    }

    private StringRequest generateSimpleStringRequest(String url) {
        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        log("Response is: "+ response.substring(0,500));
                        resultLoadView.loadData(response.toString(), null, null);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                log("That didn't work!");
                log("error:" + error.toString());
            }
        });
    }

    public void onConnectWithCookieButtonClicked(View view) {
        log(">> start w/ cookie");
        String url = getString(R.string.default_url);
        StringRequest stringRequest = generateSimpleStringRequest(url);

        // Add the request to the RequestQueue.
        requestQueueWithCookie.add(stringRequest);

        log("<< finish w/ cookie");
    }

    public void onConnectWithOutCookieButtonClicked(View view) {
        log(">> start w/o cookie");
        String url = getString(R.string.default_url);
        StringRequest stringRequest = generateSimpleStringRequest(url);

        // Add the request to the RequestQueue.
        requestQueueNoneCookie.add(stringRequest);

        log("<< finish w/o cookie");
    }

    public void onConnectWithWebViewButtonClicked(View view) {
        log(">> start by WebView ");
        String url = getString(R.string.default_url);
        resultLoadView.getSettings().setJavaScriptEnabled(true);
        resultLoadView.setWebViewClient(new MyWebViewClient());
        resultLoadView.loadUrl(url);
        log("<< finish by WebView");
    }

    public void onClearButtonClicked(View view) {
        resultLoadView.loadUrl("");
    }
}
