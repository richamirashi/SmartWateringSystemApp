package com.sws.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.sws.app.R;
import com.sws.app.commons.Session;
import com.sws.app.db.DDBManager;
import com.sws.app.db.model.DeviceItem;
import com.sws.app.iot.IotManager;

import java.util.List;

public class WaterPlantActivity extends BaseActivity {

    private static final String TAG_NAME = "WaterPlantActivity";

    private ArrayAdapter<DeviceItem> deviceNameAdapter;

    private ArrayAdapter<CharSequence> portAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "WaterPlantActivity.onCreate called ");

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.water_plant, contentFrameLayout);

        Button waterPlantButton = (Button) this.findViewById(R.id.button_water_plant);
        waterPlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "onClick: inside listener function for water plant");
                waterPlant();
            }
        });

        Button cancelButton = (Button) this.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "onClick: inside listener function for cancel");

                // Select intent based on caller activity/drawer layout
                Session session = Session.fromJson(getIntent().getStringExtra("session"));
                Intent intent = null;
                if(session.getPlantItem() != null) {
                    // When parent caller is PlantInfoActivity
                    intent = new Intent(WaterPlantActivity.this, PlantInfoActivity.class);
                } else {
                    // When caller is drawer
                    intent = new Intent(WaterPlantActivity.this, DevicesListActivity.class);
                }

                // Start next session
                startNextActivity(TAG_NAME, intent, session);
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

        // Select intent based on caller activity/drawer layout
        Session session = Session.fromJson(getIntent().getStringExtra("session"));
        Intent intent = null;
        if(session.getPlantItem() != null) {
            // When parent caller is PlantInfoActivity
            intent = new Intent(WaterPlantActivity.this, PlantInfoActivity.class);
        } else {
            // When caller is drawer
            intent = new Intent(WaterPlantActivity.this, DevicesListActivity.class);
        }

        // Start next session
        startNextActivity(TAG_NAME, intent, session);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get session details
        Session session = Session.fromJson(getIntent().getStringExtra("session"));
        String username = session.getUsername();
        String deviceId = (session.getPlantItem() == null? null: session.getPlantItem().getDeviceId());
        String plantPort = (session.getPlantItem() == null ? null : session.getPlantItem().getPlantPort());

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
        deviceNameAdapter = new ArrayAdapter<DeviceItem>(this, R.layout.spinner_style, deviceItemList);
        deviceNameAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        deviceNameSpinner.setAdapter(deviceNameAdapter);

        // For plant port spinner
        Spinner portSpinner = (Spinner) findViewById(R.id.spinner_plant_port);
        portAdapter = ArrayAdapter.createFromResource(this, R.array.plant_port_array, R.layout.spinner_style);
        portAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
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

    public void waterPlant() {
        String duration = ((EditText) findViewById(R.id.tv_duration)).getText().toString();

        Spinner deviceNameSpinner = (Spinner) findViewById(R.id.spinner_deviceName);
        DeviceItem deviceItem = deviceNameAdapter.getItem(deviceNameSpinner.getSelectedItemPosition());
        String deviceId = deviceItem.getDeviceId();

        Spinner portSpinner = (Spinner) findViewById(R.id.spinner_plant_port);
        String plantPort = portSpinner.getSelectedItem().toString();

        try {
            // Water plant through AWS IOT
            IotManager iotManager = IotManager.getInstance();
            iotManager.waterPlant(deviceId, plantPort, duration);
            String resultMessage = "Water Plant success !";
            Log.i(TAG_NAME, resultMessage + " | deviceId=" + deviceId + " |  plantPort=" + plantPort + " | duration=" + duration);

            // Select intent based on caller activity/drawer layout
            Session session = Session.fromJson(getIntent().getStringExtra("session"));
            Intent intent = null;
            if(session.getPlantItem() != null) {
                // When parent caller is PlantInfoActivity
                intent = new Intent(WaterPlantActivity.this, PlantInfoActivity.class);
            } else {
                // When caller is drawer
                intent = new Intent(WaterPlantActivity.this, DevicesListActivity.class);
            }

            // Sleep for 2 seconds for device to water plant
            Thread.sleep(2000);
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();

            // Start next session
            startNextActivity(TAG_NAME, intent, session);
        } catch (Exception e) {
            String resultMessage = "Error Watering plant !";
            Log.i(TAG_NAME, resultMessage + "deviceId=" + deviceId + " plantPort=" + plantPort + "duration=" + duration);
            e.printStackTrace();
        }
    }
}
