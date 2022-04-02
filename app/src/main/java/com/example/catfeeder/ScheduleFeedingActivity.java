package com.example.catfeeder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.widget.TextView;
import android.os.Bundle;

import com.google.android.material.slider.Slider;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.*;

public class ScheduleFeedingActivity extends AppCompatActivity {
    Button button_feeding1;
    Button button_feeding2;
    Button button_feeding3;
    Button back_button;
    TextView time_feeding1;
    TextView time_feeding2;
    TextView time_feeding3;
    TimePicker timePicker;
    String curHour;
    String curMin;
    String feedingTime1;
    String feedingTime2;
    String feedingTime3;
    Context context;
    Slider feedSizeSlider;
    Button setFeedSize;
    TextView feedSizeText;
    private final String url = "http://192.168.86.145:5000"; // URL HERE
    private final String POST = "POST";
    private final String GET = "GET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_feeding);
        context = this;

        button_feeding1 = findViewById(R.id.button_feeding1);
        button_feeding2 = findViewById(R.id.button_feeding2);
        button_feeding3 = findViewById(R.id.button_feeding3);
        back_button = findViewById(R.id.backButton);
        time_feeding1 = (TextView) findViewById(R.id.time_feeding1);
        time_feeding2 = (TextView) findViewById(R.id.time_feeding2);
        time_feeding3 = (TextView) findViewById(R.id.time_feeding3);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        feedSizeSlider = findViewById(R.id.feed_size_slider);
        setFeedSize = findViewById(R.id.set_feed_size);
        feedSizeText = findViewById(R.id.feedSizeText);


        time_feeding1.setText(getDataFromPreferences(context, "feedingTime1"));
        time_feeding2.setText(getDataFromPreferences(context, "feedingTime2"));
        time_feeding3.setText(getDataFromPreferences(context, "feedingTime3"));

        back_button.setOnClickListener(v -> openMenuActivity());

        button_feeding1.setOnClickListener(v -> {
            curHour = timePicker.getCurrentHour().toString();
            curMin = timePicker.getCurrentMinute().toString();
            if (Integer.parseInt(curMin) < 10) {
                curMin = "0"+curMin;
            }
            feedingTime1 = curHour + ":" + curMin;
            time_feeding1.setText(feedingTime1);
            saveDataToPreferences(context, "feedingTime1", feedingTime1);
            sendRequest(POST, "setFeeding", "feedingTime1","1."+feedingTime1);
        });

        button_feeding2.setOnClickListener(v -> {
            curHour = timePicker.getCurrentHour().toString();
            curMin = timePicker.getCurrentMinute().toString();
            if (Integer.parseInt(curMin) < 10) {
                curMin = "0"+curMin;
            }
            feedingTime2 = curHour + ":" + curMin;
            time_feeding2.setText(feedingTime2);
            saveDataToPreferences(context, "feedingTime2", feedingTime2);
            sendRequest(POST, "setFeeding", "feedingTime2","2."+feedingTime2);
        });

        button_feeding3.setOnClickListener(v -> {
            curHour = timePicker.getCurrentHour().toString();
            curMin = timePicker.getCurrentMinute().toString();
            if (Integer.parseInt(curMin) < 10) {
                curMin = "0"+curMin;
            }
            feedingTime3 = curHour + ":" + curMin;
            time_feeding3.setText(feedingTime3);
            saveDataToPreferences(context, "feedingTime3", feedingTime3);
            sendRequest(POST, "setFeeding", "feedingTime3","3."+feedingTime3);
        });

        setFeedSize.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                float t = feedSizeSlider.getValue();
                String t1=String.valueOf(t);
                saveDataToPreferences(context,"feed-size",t1);
                String s = "Current size:" + t1.substring(0,3);
                feedSizeText.setText(s);
            }
        });

        String t1=getDataFromPreferences(context,"feed-size");
        if (t1==null || t1.equals("")){
            feedSizeSlider.setValue(0.5F);
            String s = "Current size: 0.5";
            feedSizeText.setText(s);
        }else{
            feedSizeSlider.setValue(Float.parseFloat(t1));
            String t1s = String.valueOf(t1);
            String s = "Current size: "+t1s.substring(0,3);
            feedSizeText.setText(s);

        }


    }

    public void openMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public static void saveDataToPreferences(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("feedingPrefereces", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDataFromPreferences(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("feedingPrefereces", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    void sendRequest(String type, String method, String paramname, String param) {
        String fullURL = url + "/" + method + (param == null ? "" : "/" + param);
        Request request;

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS).build();

        /* If it is a post request, then we have to pass the parameters inside the request body*/
        if(type.equals(POST)) {
            RequestBody formBody = new FormBody.Builder()
                    .add(paramname, param)
                    .build();

            request=new Request.Builder()
                    .url(fullURL)
                    .post(formBody)
                    .build();
        }
        else {
            /*If it's our get request, it doesn't require parameters, hence just sending with the url*/
            request = new Request.Builder()
                    .url(fullURL)
                    .build();
        }

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