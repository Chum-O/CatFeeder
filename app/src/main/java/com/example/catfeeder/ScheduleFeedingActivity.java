package com.example.catfeeder;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    TextView size_feeding1;
    TextView size_feeding2;
    TextView size_feeding3;
    TimePicker timePicker;
    String curHour;
    String curMin;
    String feedingTime1;
    String feedingTime2;
    String feedingTime3;
    Context context;
    Slider feedSizeSlider;
    Button deleteButton1;
    Button deleteButton2;
    Button deleteButton3;
    Button setFeedSize1;
    Button setFeedSize2;
    Button setFeedSize3;

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
        size_feeding1 = (TextView) findViewById(R.id.size_feeding1);
        size_feeding2 = (TextView) findViewById(R.id.size_feeding2);
        size_feeding3 = (TextView) findViewById(R.id.size_feeding3);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        feedSizeSlider = findViewById(R.id.feed_size_slider);
        deleteButton1 = findViewById(R.id.remove_feeding1);
        deleteButton2 = findViewById(R.id.remove_feeding2);
        deleteButton3 = findViewById(R.id.remove_feeding3);
        setFeedSize1 = findViewById(R.id.button_feeding1_size);
        setFeedSize2 = findViewById(R.id.button_feeding2_size);
        setFeedSize3 = findViewById(R.id.button_feeding3_size);

        String t1 = "";
        try {
            t1 = convert24to12hr(getDataFromPreferences(context,"feedingTime1"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String t2 = "";
        try {
            t2 = convert24to12hr(getDataFromPreferences(context,"feedingTime2"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String t3 = "";
        try {
            t3 = convert24to12hr(getDataFromPreferences(context,"feedingTime3"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        time_feeding1.setText(t1);
        time_feeding2.setText(t2);
        time_feeding3.setText(t3);

        back_button.setOnClickListener(v -> openMenuActivity());

        button_feeding1.setOnClickListener(v -> {
            curHour = timePicker.getCurrentHour().toString();
            curMin = timePicker.getCurrentMinute().toString();
            if (Integer.parseInt(curMin) < 10) {
                curMin = "0"+curMin;
            }
            feedingTime1 = curHour + ":" + curMin;
            String cn = feedingTime1;
            try {
                cn = convert24to12hr(feedingTime1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            time_feeding1.setText(cn);
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
            String cn = feedingTime1;
            try {
                cn = convert24to12hr(feedingTime2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            time_feeding2.setText(cn);
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
            String cn = feedingTime1;
            try {
                cn = convert24to12hr(feedingTime3);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            time_feeding3.setText(cn);
            saveDataToPreferences(context, "feedingTime3", feedingTime3);
            sendRequest(POST, "setFeeding", "feedingTime3","3."+feedingTime3);
        });

        setFeedSize1.setOnClickListener(v -> {
            String feedingSize1 = String.valueOf(feedSizeSlider.getValue());
            saveDataToPreferences(context, "feedingSize1", feedingSize1);
            sendRequest(POST, "setFeedSize", "size","1:"+feedingSize1);
            size_feeding1.setText(feedingSize1.substring(0,4));
        });

        setFeedSize2.setOnClickListener(v -> {
            String feedingSize2 = String.valueOf(feedSizeSlider.getValue());
            saveDataToPreferences(context, "feedingSize2", feedingSize2);
            sendRequest(POST, "setFeedSize", "size","2:"+feedingSize2);
            size_feeding2.setText(feedingSize2.substring(0,4));
        });

        setFeedSize3.setOnClickListener(v -> {
            String feedingSize3 = String.valueOf(feedSizeSlider.getValue());
            saveDataToPreferences(context, "feedingSize3", feedingSize3);
            sendRequest(POST, "setFeedSize", "size","3:"+feedingSize3);
            size_feeding3.setText(feedingSize3.substring(0,4));
        });

        deleteButton1.setOnClickListener(v -> {
            feedingTime1 = "";
            saveDataToPreferences(context,"feedingTime1",null);
            time_feeding1.setText("");
            sendRequest(POST,"deleteFeeding", "feedID", "1");

        });

        deleteButton2.setOnClickListener(v -> {
            feedingTime2 = "";
            saveDataToPreferences(context,"feedingTime2",null);
            time_feeding2.setText("");
            sendRequest(POST,"deleteFeeding", "feedID", "2");

        });

        deleteButton3.setOnClickListener(v -> {
            feedingTime3 = "";
            saveDataToPreferences(context,"feedingTime3",null);
            time_feeding3.setText("");
            sendRequest(POST,"deleteFeeding", "feedID", "3");

        });


        if (getDataFromPreferences(context,"feedingSize1") != null && getDataFromPreferences(context,"feedingSize1").length() > 3 ){
            size_feeding1.setText(getDataFromPreferences(context,"feedingSize1").substring(0,4));
            feedSizeSlider.setValue(Float.parseFloat(getDataFromPreferences(context,"feedingSize1")));
        }
        if (getDataFromPreferences(context,"feedingSize2") != null  && getDataFromPreferences(context,"feedingSize1").length() > 3){
            size_feeding2.setText(getDataFromPreferences(context,"feedingSize2").substring(0,4));
        }
        if (getDataFromPreferences(context,"feedingSize3") != null  && getDataFromPreferences(context,"feedingSize1").length() > 3){
            size_feeding3.setText(getDataFromPreferences(context,"feedingSize3").substring(0,4));
        }

    }

    public void openMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public String convert24to12hr(String s) throws ParseException {
        String _24HourTime = s;
        SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
        SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
        Date _24HourDt = _24HourSDF.parse(_24HourTime);
        System.out.println(_24HourDt);
        return _12HourSDF.format(_24HourDt);
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