package com.example.communicatewithcookiebyhttpclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onConnectWithCookieButtonClicked(View view) {
        Log.d(TAG, ">> start w/ cookie");

        Log.d(TAG, "<< finish w/ cookie");
    }

    public void onConnectWithOutCookieButtonClicked(View view) {
        Log.d(TAG, ">> start w/o cookie");

        Log.d(TAG, "<< finish w/o cookie");
    }
}
