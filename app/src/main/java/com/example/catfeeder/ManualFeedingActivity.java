package com.example.catfeeder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ManualFeedingActivity extends AppCompatActivity {
    Button dispense;
    TextView statusText;
    WebView PiStream;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_feeding_activity);
        dispense = findViewById(R.id.button_trigger_manual_feed);
        statusText = findViewById(R.id.statusText);
        PiStream = (WebView) findViewById(R.id.pi_cam_stream);

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


    }

}

