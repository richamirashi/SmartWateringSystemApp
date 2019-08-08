package com.sws.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.sws.app.R;
import com.sws.app.commons.Session;
import com.sws.app.db.DDBManager;
import com.sws.app.db.model.DeviceItem;
import com.sws.app.db.model.PlantItem;
import com.sws.app.iot.IotManager;

import java.util.List;

public class SoilMoistureStatsActivity extends BaseActivity {
    private static final String TAG_NAME = "SoilMoistureStatsActvty";

    private ArrayAdapter<DeviceItem> deviceNameAdapter;

    private ArrayAdapter<CharSequence> portAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "SoilMoistureStatsActivity.onCreate called ");
//        setContentView(R.layout.soil_moisture_stats);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.soil_moisture_stats, contentFrameLayout);

        Button getMoistureStatsButton = (Button) this.findViewById(R.id.button_get_moisture_stats);
        getMoistureStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "onClick: inside listener function for moisture stats button");
                getMoistureStats();
            }
        });

        Button cancelButton = (Button) this.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "onClick: inside listener function for cancel");
                // TODO: Fix this for going back to the activity that called it.
                Intent intent = new Intent(SoilMoistureStatsActivity.this, PlantInfoActivity.class);
                Session session = Session.fromJson(getIntent().getStringExtra("session"));
                Log.i(TAG_NAME, "Cancel Session: " + session.toJson());
                intent.putExtra("session", session.toJson());
                startActivity(intent);
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
        Intent intent = new Intent(SoilMoistureStatsActivity.this, PlantInfoActivity.class);
        intent.putExtra("session", session.toJson());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get session details
        Session session = Session.fromJson(getIntent().getStringExtra("session"));
        String username = session.getUsername();
        String deviceId = session.getPlantItem().getDeviceId();
        String plantPort = session.getPlantItem().getPlantPort();

        // Get list of devices from database to populate the deviceIdSpinner
        List<DeviceItem> deviceItemList;
        try {
            DDBManager ddbManager = DDBManager.getInstance();
            deviceItemList = ddbManager.listDevices(username);
        } catch (DDBManager.DeviceListException e) {
            String resultMessage = "Error occurred while fetching the device list !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }

        // For device spinner
        Spinner deviceNameSpinner = (Spinner) findViewById(R.id.spinner_deviceName);
        deviceNameAdapter = new ArrayAdapter<DeviceItem>(this, android.R.layout.simple_spinner_dropdown_item, deviceItemList);
        deviceNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deviceNameSpinner.setAdapter(deviceNameAdapter);

        // For plant port spinner
        Spinner portSpinner = (Spinner) findViewById(R.id.spinner_plant_port);
        portAdapter = ArrayAdapter.createFromResource(this, R.array.plant_port_array, android.R.layout.simple_spinner_dropdown_item);
        portAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        portSpinner.setAdapter(portAdapter);

        // select values from session
        if (deviceId != null) {
            int position = deviceNameAdapter.getPosition(session.getDeviceItem());
            deviceNameSpinner.setSelection(position);
            deviceNameSpinner.setEnabled(false);
        }

        if (plantPort != null) {
            int position = portAdapter.getPosition(plantPort);
            portSpinner.setSelection(position);
            portSpinner.setEnabled(false);
        }
    }

    public void getMoistureStats() {
        Spinner deviceNameSpinner = (Spinner) findViewById(R.id.spinner_deviceName);
        DeviceItem deviceItem = deviceNameAdapter.getItem(deviceNameSpinner.getSelectedItemPosition());
        String deviceId = deviceItem.getDeviceId();

        Spinner portSpinner = (Spinner) findViewById(R.id.spinner_plant_port);
        String plantPort = portSpinner.getSelectedItem().toString();
        String resultMessage;

        // make request to device to update soil moisture stat in database
        try {
            IotManager iotManager = IotManager.getInstance();
            iotManager.requestDeviceForSoilMoistureStat(deviceId, plantPort);
        } catch (Exception e) {
            resultMessage = "Error Getting Moisture Stats !";
            Log.i(TAG_NAME, resultMessage + "deviceId=" + deviceId + " plantPort=" + plantPort);
            e.printStackTrace();
            return;
        }

        // sleep for few seconds
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // query database
        try {
            DDBManager ddbManager = DDBManager.getInstance();
            PlantItem plantItem = ddbManager.getPlantItem(deviceId, plantPort);

            // show soil moisture stat, at this point plantItem will always be not null
            resultMessage = "Moisture Stat: " + plantItem.getMoistureStat();
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SoilMoistureStatsActivity.this, PlantInfoActivity.class);
            Session session = Session.fromJson(getIntent().getStringExtra("session"));
            Log.i(TAG_NAME, "Session: " + session.toJson());
            intent.putExtra("session", session.toJson());
            startActivity(intent);
        } catch (DDBManager.PlantGetException e) {
            resultMessage = "Error occurred while fetching pant info!";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
    }
}
