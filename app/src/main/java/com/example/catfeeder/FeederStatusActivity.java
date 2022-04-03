package com.example.catfeeder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FeederStatusActivity extends AppCompatActivity {

    Button backButton;
    Button checkStatusButton;
    TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feeder_status);
        statusText = (TextView) findViewById(R.id.status_text);
        checkStatusButton = findViewById(R.id.check_status_button);
        backButton = findViewById(R.id.back_button);
        super.onCreate(savedInstanceState);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
            }
        });

        checkStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient okhttpclient = new OkHttpClient();
                Request request = new Request.Builder().url("http://192.168.86.145:5000/").build();

                okhttpclient.newCall(request).enqueue(new Callback() {
                    @Override
                    // called if server is unreachable
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "no response received", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getApplicationContext(), "Feeder response received", Toast.LENGTH_SHORT).show();
                                    Log.println(Log.DEBUG,"debug","response received");
                                }
                            });
                        }
                    }
                });
            }
        });

    }

}