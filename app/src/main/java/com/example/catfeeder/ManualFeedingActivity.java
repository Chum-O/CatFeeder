package com.example.catfeeder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ManualFeedingActivity extends AppCompatActivity {
    Button dispense;
    WebView PiStream;
    com.google.android.material.slider.Slider feedSizeSlider;
    TextView FeedSizeText;
    Context context;
    Button SetFeedSize;

    private void dispenseFood(){
        dispense.setEnabled(false);
        dispense.postDelayed(() -> {
            dispense.setEnabled(true);
        }, 3500);

        OkHttpClient okhttpclient = new OkHttpClient();
        Request request = new Request.Builder().url("http://192.168.86.145:5000/feed").build();

        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            // called if server is unreachable
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.println(Log.DEBUG,"debug","error connecting to the server");
                    }
                });
            }

            @Override
            // called if we get a
            // response from the server
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body().string().equals("received")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "feed successful", Toast.LENGTH_SHORT).show();
                            Log.println(Log.DEBUG,"debug","response received");
                        }
                    });
                }
            }
        });
        return;
    }

    private void setFeedSizeTemp(String size){
        OkHttpClient okhttpclient = new OkHttpClient();
        Request request;
        String fullURL = "http://192.168.86.145:5000/setFeedSizeTemp" + (size==null ? "" : "/"+size);

        RequestBody formBody = new FormBody.Builder()
                .add("size", size)
                .build();

        request=new Request.Builder()
                .url(fullURL)
                .post(formBody)
                .build();

        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            // called if server is unreachable
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.println(Log.DEBUG,"debug","error connecting to the server");
                    }
                });
            }

            @Override
            // called if we get a
            // response from the server
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.body().string().equals("success")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "temporary feed size set", Toast.LENGTH_SHORT).show();
                            Log.println(Log.DEBUG,"debug","response received");
                        }
                    });
                }
            }
        });
        return;
    }

    public static void saveDataToPreferences(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("feed-size", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDataFromPreferences(Context context, String key) {
        try {
            SharedPreferences sp = context.getSharedPreferences("feed-size", Context.MODE_PRIVATE);
            return sp.getString(key, "");
        } catch(Exception ex){
            return "0";
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_feeding_activity);
        dispense = findViewById(R.id.button_trigger_manual_feed);
        feedSizeSlider = findViewById(R.id.feed_size_slider);
        PiStream = (WebView) findViewById(R.id.pi_cam_stream);
        FeedSizeText = findViewById(R.id.feedSizeText);
        SetFeedSize = findViewById(R.id.set_feed_size);
        context=this;

        String piCamPythonStream = "http://192.168.86.145:8000/stream.mjpg";
        String motionStream = "http://192.168.86.145:8081";
        String mjpgStreamer = "http://192.168.86.145:8080/?action=stream";
        PiStream.getSettings().setLoadWithOverviewMode(true);
        PiStream.getSettings().setUseWideViewPort(true);

        PiStream.loadUrl(mjpgStreamer);

        dispense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispenseFood();
            }
        });

        SetFeedSize.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                float t = feedSizeSlider.getValue();
                String t1=String.valueOf(t);
                saveDataToPreferences(context,"feed-size",t1);
                String t1s = String.valueOf(t);
                setFeedSizeTemp(t1s);
                String s = "Current size: " + t1s.substring(0,3);
                FeedSizeText.setText(s);
            }
        });

        String t1=getDataFromPreferences(context,"feed-size");
        if (t1==null || t1.equals("")){
            feedSizeSlider.setValue(0.5F);
            String s = "Current size: 0.5";
            FeedSizeText.setText(s);
        }else{
            feedSizeSlider.setValue(Float.parseFloat(t1));
            float t = Math.round(Float.parseFloat(t1)*100)/100;
            String t1s = String.valueOf(t);
            String s = "Current size: "+t1s.substring(0,3);
            FeedSizeText.setText(s);

        }



    }


}

