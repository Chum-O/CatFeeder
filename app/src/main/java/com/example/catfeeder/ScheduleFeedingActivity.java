package com.example.catfeeder;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TimePicker;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.os.Bundle;

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
    String feedingTimeOperative1;
    String feedingTime2;
    String feedingTimeOperative2;
    String feedingTime3;
    String feedingTimeOperative3;
    Intent intent;
    Context context;

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


        time_feeding1.setText(getDataFromPreferences(context, "feedingTime1"));

        button_feeding1.setOnClickListener(v -> {
            curHour = timePicker.getCurrentHour().toString();
            curMin = timePicker.getCurrentMinute().toString();
            feedingTime1 = curHour + ":" + curMin;
            time_feeding1.setText(feedingTime1);
            saveDataToPreferences(context, "feedingTime1", feedingTime1);
        });

        button_feeding2.setOnClickListener(v -> {
            curHour = timePicker.getCurrentHour().toString();
            curMin = timePicker.getCurrentMinute().toString();
            feedingTime2 = curHour + ":" + curMin;
            time_feeding2.setText(feedingTime2);
            saveDataToPreferences(context, "feedingTime2", feedingTime2);
        });

        button_feeding3.setOnClickListener(v -> {
            curHour = timePicker.getCurrentHour().toString();
            curMin = timePicker.getCurrentMinute().toString();
            feedingTime3 = curHour + ":" + curMin;
            time_feeding3.setText(feedingTime3);
            saveDataToPreferences(context, "feedingTime3", feedingTime3);
        });

    }

    public static void saveDataToPreferences(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("feedings", Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDataFromPreferences(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("feedings", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

}