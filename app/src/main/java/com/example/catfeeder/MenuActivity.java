package com.example.catfeeder;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {
    Button button_sign_out;
    Button button_feeder_status;
    Button button_feeder_log;
    Button button_schedule_feedings;
    Button button_activate_feeder;
    Button button_activate_laser;
    Button button_view_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        button_sign_out = findViewById(R.id.button_sign_out);
        button_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainActivity();
            }
        });
        button_feeder_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeederStatusActivity();
            }
        });
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openFeederStatusActivity() {
        Intent intent = new Intent(this, FeederStatusActivity.class);
        startActivity(intent);
    }
}