package com.example.catfeeder;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;

public class MenuActivity extends AppCompatActivity {
    Button button_sign_out;
    Button button_feeder_status;
    Button button_feeder_log;
    Button button_schedule_feeding;
    Button button_manual_feeding;
    Button button_camera_laser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        button_sign_out = findViewById(R.id.button_sign_out);
        button_feeder_status = findViewById(R.id.button_feeder_status);
        button_feeder_log = findViewById(R.id.button_feeder_log);
        button_schedule_feeding = findViewById(R.id.button_schedule_feeding);
        button_manual_feeding = findViewById(R.id.button_activate_feeder);
        button_camera_laser = findViewById(R.id.button_laser_camera);

        button_sign_out.setOnClickListener(v -> openMainActivity());

        button_feeder_status.setOnClickListener(v -> openFeederStatusActivity());

        button_feeder_log.setOnClickListener(v -> openFeederLogActivity());

        button_schedule_feeding.setOnClickListener(v -> openScheduleFeedingActivity());

        button_manual_feeding.setOnClickListener(v -> openFeedingActivity());

        button_camera_laser.setOnClickListener(v -> openLaserToyActivity());
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openFeederStatusActivity() {
        Intent intent = new Intent(this, FeederStatusActivity.class);
        startActivity(intent);
    }

    public void openFeederLogActivity() {
        Intent intent = new Intent(this, FeederLogActivity.class);
        startActivity(intent);
    }

    public void openScheduleFeedingActivity() {
        Intent intent = new Intent(this, ScheduleFeedingActivity.class);
        startActivity(intent);
    }
    public void openFeedingActivity() {
        Intent intent = new Intent(this, ManualFeedingActivity.class);
        startActivity(intent);
    }
    public void openLaserToyActivity() {
        Intent intent = new Intent(this, LaserToyActivity.class);
        startActivity(intent);
    }
}