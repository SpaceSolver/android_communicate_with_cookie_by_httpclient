package com.example.communicatewithcookiebyhttpclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView logView;
    private WebView resultLoadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logView = (TextView)findViewById(R.id.LogTextView);
        resultLoadView = (WebView)findViewById(R.id.ResultWebView);
    }

    private void log(String s) {
        Log.d(TAG, s );
        logView.append(s + "\n");
    }

    public void onConnectWithCookieButtonClicked(View view) {
        log(">> start w/ cookie");

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        log("<< finish w/ cookie");
    }

    public void onConnectWithOutCookieButtonClicked(View view) {
        log(">> start w/o cookie");

        log("<< finish w/o cookie");
    }

    public void onConnectWithWebViewButtonClicked(View view) {
        log(">> start by WebView ");
        String url = getString(R.string.default_url);
        resultLoadView.getSettings().setJavaScriptEnabled(true);
        resultLoadView.setWebViewClient(new MyWebViewClient());
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.setAcceptThirdPartyCookies(resultLoadView, true);
        resultLoadView.loadUrl(url);
        log("<< finish by WebView");
    }

    public void onClearButtonClicked(View view) {
        resultLoadView.loadUrl("");
    }
}
