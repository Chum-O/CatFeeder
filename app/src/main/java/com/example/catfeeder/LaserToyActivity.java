package com.example.catfeeder;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LaserToyActivity extends AppCompatActivity {
    WebView PiStream;
    Button randomJitterButton;

    private float[] lastTouchDownXY = new float[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laser_toy_activity);

        randomJitterButton = findViewById(R.id.random_jitter_button);
        randomJitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomJitter();
            }
        });

        // add both a touch listener and a click listener
        View myView = findViewById(R.id.pi_cam_controller);
        myView.setOnTouchListener(touchListener);
        myView.setOnClickListener(clickListener);

        PiStream = (WebView) findViewById(R.id.pi_cam_stream);

        String piCamPythonStream = "http://192.168.86.145:8000/stream.mjpg";
        String motionStream = "http://192.168.86.145:8081";
        String mjpgStreamer = "http://192.168.86.145:8080/?action=stream";
        PiStream.getSettings().setLoadWithOverviewMode(true);
        PiStream.getSettings().setUseWideViewPort(true);

        PiStream.loadUrl(mjpgStreamer);

    }

    // the purpose of the touch listener is just to store the touch X,Y coordinates
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            // save the X,Y coordinates
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                lastTouchDownXY[0] = event.getX();
                lastTouchDownXY[1] = event.getY();
            }

            // let the touch event pass on to whoever needs it
            return false;
        }
    };

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // retrieve the stored coordinates
            float x = lastTouchDownXY[0];
            float y = lastTouchDownXY[1];

            // use the coordinates for whatever
            Log.i("TAG", "onLongClick: x = " + x + ", y = " + y);
        }
    };

    private void randomJitter(){
        randomJitterButton.setEnabled(false);
        randomJitterButton.postDelayed(() -> {
            randomJitterButton.setEnabled(true);
        }, 19000);

        OkHttpClient okhttpclient = new OkHttpClient();
        Request request = new Request.Builder().url("http://192.168.86.145:5000/randomJitter").build();

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
                            Toast.makeText(getApplicationContext(), "successful", Toast.LENGTH_SHORT).show();
                            Log.println(Log.DEBUG,"debug","response received");
                        }
                    });
                }
            }
        });
        return;
    }
}
