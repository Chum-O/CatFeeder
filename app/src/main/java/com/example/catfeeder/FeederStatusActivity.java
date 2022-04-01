package com.example.catfeeder;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.*;

public class FeederStatusActivity extends AppCompatActivity {
    Button back;
    ImageView statusOn;
    ImageView statusOff;
    TextView statusText;

    private final String url = "http://192.168.86.145:5000"; // URL HERE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeder_status);

        back = findViewById(R.id.back);
        statusOn = findViewById(R.id.statusOn);
        statusOff = findViewById(R.id.statusOff);
        statusText = findViewById(R.id.statusText);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            // if server is unreachable = pi is off
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.println(Log.DEBUG, "debug", "error connecting to server");
                        statusOn.setVisibility(View.VISIBLE);
                        statusOff.setVisibility(View.INVISIBLE);
                        statusText.setText("OFF");
                    }
                });
            }

            // otherwise, the pi is on as it should most likely be
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.body().string().equals("Server on")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.println(Log.DEBUG, "debug", "server running");
                            statusOff.setVisibility(View.VISIBLE);
                            statusOn.setVisibility(View.INVISIBLE);
                            statusText.setText("ON");
                        }
                    });
                }
            }
        });

        back.setOnClickListener(v -> openMainActivity());
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}