package com.sws.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sws.app.R;
import com.sws.app.commons.Session;
import com.sws.app.db.DDBManager;
import com.sws.app.db.model.PlantItem;
import com.sws.app.db.model.PlantPort;

public class PlantInfoActivity extends BaseActivity {

    private static final String TAG_NAME = "PlantInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "PlantInfoActivity.onCreate called ");

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.plant_info, contentFrameLayout);

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
        if (lastWatered != null) {
            lastWateredTextView.setText(lastWatered);
        }

        String startSchedule = session.getPlantItem().getScheduledStartTime();
        TextView startScheduleTextView = findViewById(R.id.tv_schedule);
        if (startSchedule != null) {
            startScheduleTextView.setText(startSchedule);
        }

        String wateringFrequency = session.getPlantItem().getScheduledFrequency();
        TextView wateringFrequencyTextView = findViewById(R.id.tv_frequency);
        if (wateringFrequency != null) {
            wateringFrequencyTextView.setText(wateringFrequency);
        }

        String moistureStat = session.getPlantItem().getMoistureStat();
        TextView moistureStatTextView = findViewById(R.id.tv_moisture_stats);
        if (moistureStat != null) {
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


        final DialogInterface.OnClickListener deletePlantDialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        //Yes button clicked
                        Session session = Session.fromJson(getIntent().getStringExtra("session"));
                        String deviceId = (session.getPlantItem() == null? null: session.getPlantItem().getDeviceId());
                        String plantPortStr = (session.getPlantItem() == null ? null : session.getPlantItem().getPlantPort());

                        DDBManager ddbManager = DDBManager.getInstance();

                        // delete plant from database
                        try {
                            ddbManager.deletePlantItem(deviceId, PlantPort.valueOf(plantPortStr));
                        } catch (DDBManager.PlantDeleteException e) {
                            String resultMessage = "Error occurred while deleting plant!";
                            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            return;
                        }

                        // go one page back
                        onBackPressed();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked -> do nothing
                        break;
                }
            }
        };

        Button deletePlant = (Button) this.findViewById(R.id.button_delete_plant);
        deletePlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.i(TAG_NAME, "Inside listener function for delete plant button");
            AlertDialog.Builder builder = new AlertDialog.Builder(PlantInfoActivity.this);
            builder.setMessage("Are you sure?").setPositiveButton("Yes", deletePlantDialogClickListener)
                    .setNegativeButton("No", deletePlantDialogClickListener).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG_NAME, "Back button pressed");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        Session session = Session.fromJson(getIntent().getStringExtra("session"));
        Log.i(TAG_NAME, "Back button: " + session.toJson());
        Intent intent = new Intent(PlantInfoActivity.this, PlantsListActivity.class);
        intent.putExtra("session", session.toJson());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            Session session = Session.fromJson(getIntent().getStringExtra("session"));
            String deviceId = session.getDeviceItem().getDeviceId();
            String plantPortStr = session.getPlantItem().getPlantPort();
            PlantPort plantPort = PlantPort.valueOf(plantPortStr);

            DDBManager ddbManager = DDBManager.getInstance();
            PlantItem plantItem  = ddbManager.getPlantItem(deviceId, plantPort);

            Log.i(TAG_NAME, "last watered: " + plantItem.getLastWatered());
            TextView lastWateredTextView = findViewById(R.id.tv_last_watered);
            lastWateredTextView.setText(plantItem.getLastWatered());

            TextView startScheduleTextView = findViewById(R.id.tv_schedule);
            startScheduleTextView.setText(plantItem.getScheduledStartTime());

            TextView wateringFrequencyTextView = findViewById(R.id.tv_frequency);
            wateringFrequencyTextView.setText(plantItem.getScheduledFrequency());

            TextView moistureStatTextView = findViewById(R.id.tv_moisture_stats);
            moistureStatTextView.setText(plantItem.getMoistureStat());
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
