package com.sws.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sws.app.R;
import com.sws.app.commons.Session;

public class PlantInfoActivity extends AppCompatActivity {

    private static final String TAG_NAME = "PlantInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "PlantInfoActivity.onCreate called ");
        setContentView(R.layout.plant_info);

        Session session = Session.fromJson(getIntent().getStringExtra("session"));
        Log.i(TAG_NAME, "Session: " + session.toJson());

        TextView deviceIDTextView = findViewById(R.id.tv_deviceid);
        deviceIDTextView.setText(session.getDeviceItem().getDeviceId());

        TextView deviceNameTextView = findViewById(R.id.tv_device_name);
        deviceNameTextView.setText(session.getDeviceItem().getDeviceName());

        TextView plantNameTextView = findViewById(R.id.tv_plant_name);
        plantNameTextView.setText(session.getPlantItem().getPlantName());

        TextView plantPortTextView = findViewById(R.id.tv_plant_port);
        plantPortTextView.setText(session.getPlantItem().getPlantPort());

        TextView plantTypeTextView = findViewById(R.id.tv_plant_type);
        plantTypeTextView.setText(session.getPlantItem().getType());

        TextView plantDescriptionTextView = findViewById(R.id.tv_plant_description);
        plantDescriptionTextView.setText(session.getPlantItem().getDescription());

        String lastWatered = session.getPlantItem().getLastWatered();
        TextView lastWateredTextView = findViewById(R.id.tv_last_watered);
        if(lastWatered != null){
            lastWateredTextView.setText(lastWatered);
        }

        String lastSchedule = session.getPlantItem().getScheduledStartTime();
        TextView lastScheduleTextView = findViewById(R.id.tv_schedule);
        if(lastSchedule != null){
            lastScheduleTextView.setText(lastSchedule);
        }

        String moistureStat = session.getPlantItem().getMoistureStat();
        TextView moistureStatTextView = findViewById(R.id.tv_moisture_stats);
        if(moistureStat != null){
            moistureStatTextView.setText(moistureStat);
        }

        Button waterPlantButton = (Button) this.findViewById(R.id.button_water_plant);
        waterPlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "Inside listener function for Water Plant button");
                Intent intent = new Intent(PlantInfoActivity.this, WaterPlantActivity.class);
                Session session = Session.fromJson(getIntent().getStringExtra("session"));
                Log.i(TAG_NAME, "Session: " + session.toJson());
                intent.putExtra("session", session.toJson());
                startActivity(intent);
            }
        });

        Button setScheduleButton = (Button) this.findViewById(R.id.button_set_schedule);
        setScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "Inside listener function for Set Schedule button");
                Intent intent = new Intent(PlantInfoActivity.this, SetScheduleActivity.class);
                Session session = Session.fromJson(getIntent().getStringExtra("session"));
                Log.i(TAG_NAME, "Session: " + session.toJson());
                intent.putExtra("session", session.toJson());
                startActivity(intent);
            }
        });

        Button getMoistureStatsButton = (Button) this.findViewById(R.id.button_get_moisture_stats);
        getMoistureStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "Inside listener function for Get Moisture Stats button");
                Intent intent = new Intent(PlantInfoActivity.this, SoilMoistureStatsActivity.class);
                Session session = Session.fromJson(getIntent().getStringExtra("session"));
                Log.i(TAG_NAME, "Session: " + session.toJson());
                intent.putExtra("session", session.toJson());
                startActivity(intent);
            }
        });
    }
}
