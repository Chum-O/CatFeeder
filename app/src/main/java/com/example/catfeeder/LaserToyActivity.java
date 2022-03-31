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
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LaserToyActivity extends AppCompatActivity {
    WebView PiStream;
    Button randomJitterButton;
    Button laserToggleButton;

    private float[] lastTouchDownXY = new float[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laser_toy_activity);
        randomJitterButton = findViewById(R.id.random_jitter_button);
        laserToggleButton = findViewById(R.id.enable_disable_laser);

        randomJitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomJitter();
            }
        });

        laserToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLaser();
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

            sendData(x,y);

        }
    };

    void sendData(float x, float y){
        String url = "http://192.168.86.145:5000";
        String method = "setServo";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();
        Request request;

        int data1 = (int)(x / 950 * 100);
        int data2 = (int)(y / 530 * 100);
        Log.i("TAG", "onLongClick: data1 = " + data1 + ", data2 = " + data2);

        String param = Integer.toString(data1)+":"+Integer.toString(data2);
        String fullURL = url + "/" + method + (param == null ? "" : "/" + param);

        RequestBody formBody = new FormBody.Builder()
                .add("ServoData", param)
                .build();

        request=new Request.Builder()
                .url(fullURL)
                .post(formBody)
                .build();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    try {
                        client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

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

    private void toggleLaser(){
        String url = "http://192.168.86.145:5000";
        String method = "toggleLaser";

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();
        Request request;

        Log.i("TAG", "toggling laser");

        String fullURL = url + "/" + method;


        request=new Request.Builder()
                .url(fullURL)
                .build();

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    try {
                        client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
