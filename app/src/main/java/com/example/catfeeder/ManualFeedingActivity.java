package com.example.catfeeder;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ManualFeedingActivity extends AppCompatActivity {
    OkHttpClient okhttpclient = new OkHttpClient();
    Request request = new Request.Builder().url("http://192.168.86.145:5000/").build();
}
