package com.example.catfeeder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class FeederLogActivity extends AppCompatActivity {
    WebView logViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeder_log);
        logViewer = findViewById(R.id.logViewer);
        logViewer.loadUrl("http://192.168.86.145:5000/getTimes");
    }
}