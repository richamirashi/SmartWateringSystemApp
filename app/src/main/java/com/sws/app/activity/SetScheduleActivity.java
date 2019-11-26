package com.sws.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sws.app.R;
import com.sws.app.commons.Session;
import com.sws.app.commons.Utils;
import com.sws.app.db.DDBManager;
import com.sws.app.db.model.DeviceItem;
import com.sws.app.db.model.PlantItem;
import com.sws.app.iot.IotManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SetScheduleActivity extends BaseActivity {

    private static final String TAG_NAME = "SetScheduleActivity";

    private ArrayAdapter<DeviceItem> deviceNameAdapter;

    private ArrayAdapter<CharSequence> portAdapter;

    private ArrayAdapter<CharSequence> frequencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "SetScheduleActivity.onCreate called ");

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.set_schedule, contentFrameLayout);

        Button scheduleButton = (Button) this.findViewById(R.id.button_schedule);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "onClick: inside listener function for schedule");
                setSchedule();
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
                    intent = new Intent(SetScheduleActivity.this, PlantInfoActivity.class);
                } else {
                    // When caller is drawer
                    intent = new Intent(SetScheduleActivity.this, DevicesListActivity.class);
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
            intent = new Intent(SetScheduleActivity.this, PlantInfoActivity.class);
        } else {
            // When caller is drawer
            intent = new Intent(SetScheduleActivity.this, DevicesListActivity.class);
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

        // For frequency spinner
        Spinner frequencySpinner = (Spinner) findViewById(R.id.spinner_frequency);
        frequencyAdapter = ArrayAdapter.createFromResource(this, R.array.schedule_frequency_array, R.layout.spinner_style);
        frequencyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        frequencySpinner.setAdapter(frequencyAdapter);

        // select values from session when navigate from plant info activity to set schedule activity
        if (deviceId != null) {
            int position = deviceNameAdapter.getPosition(session.getDeviceItem());
            deviceNameSpinner.setSelection(position);
            deviceNameSpinner.setEnabled(false);
        }

        // select values from session when navigate from plant info activity to set schedule activity
        if (plantPort != null) {
            int position = portAdapter.getPosition(plantPort);
            portSpinner.setSelection(position);
            portSpinner.setEnabled(false);
        }

        // when navigate from drawer layout to set schedule activity and when no device is registered
        if(deviceItemList == null || deviceItemList.size() <= 0){
            Log.i(TAG_NAME, "deviceItemList size: " + deviceItemList.size());
            Button scheduleButton = (Button) this.findViewById(R.id.button_schedule);
            scheduleButton.setEnabled(false);
            portSpinner.setEnabled(false);
            frequencySpinner.setEnabled(false);
        }
        // when navigate from drawer layout to set schedule activity and when no plants are registered
        else if(deviceId == null && (deviceItemList != null || deviceItemList.size() > 0)){
            DeviceItem deviceItem = deviceNameAdapter.getItem(deviceNameSpinner.getSelectedItemPosition());
            String selecteddeviceId = deviceItem.getDeviceId();

            List<PlantItem> plantItemList;
            try{
                Log.i(TAG_NAME, "selecteddeviceId: " + selecteddeviceId);
                DDBManager ddbManager = DDBManager.getInstance();
                plantItemList = ddbManager.listPlants(selecteddeviceId);
            } catch(DDBManager.PlantListException e){
                String resultMessage = "Error occurred while fetching the plant list!";
                Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            }

            if(plantItemList == null || plantItemList.size() <= 0){
                Log.i(TAG_NAME, "plantItemList size: " + plantItemList.size());
                Button scheduleButton = (Button) this.findViewById(R.id.button_schedule);
                scheduleButton.setEnabled(false);
                portSpinner.setEnabled(false);
                frequencySpinner.setEnabled(false);
            }
        }
    }

    public void setSchedule() {
        String resultMessage;

        Spinner deviceNameSpinner = (Spinner) findViewById(R.id.spinner_deviceName);
        DeviceItem deviceItem = deviceNameAdapter.getItem(deviceNameSpinner.getSelectedItemPosition());
        String deviceId = deviceItem.getDeviceId();

        Spinner portSpinner = (Spinner) findViewById(R.id.spinner_plant_port);
        String plantPort = portSpinner.getSelectedItem().toString();

        DatePicker datePicker = findViewById(R.id.schedule_date);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        TimePicker timePicker = findViewById(R.id.schedule_time);
//        timePicker.setIs24HourView(true);
        int hour = timePicker.getHour();
        int min = timePicker.getMinute();

        TimeZone.setDefault(TimeZone.getTimeZone("PST"));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, min, 0);
        Date scheduleStartDate = calendar.getTime();
        String scheduleStartDateStr = Utils.getFormattedDate(scheduleStartDate);

        String duration = ((EditText) findViewById(R.id.tv_duration)).getText().toString();
        Spinner frequencySpinner = findViewById(R.id.spinner_frequency);
        String frequency = frequencySpinner.getSelectedItem().toString();

        try {
            DDBManager ddbManager = DDBManager.getInstance();
            ddbManager.setSchedule(deviceId, plantPort, scheduleStartDateStr, frequency, duration);
            resultMessage = "Schedule created !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            IotManager iotManager = IotManager.getInstance();
            iotManager.createSchedule(deviceId, plantPort, scheduleStartDateStr, frequency, duration);
            Log.i(TAG_NAME, resultMessage + "deviceId=" + deviceId + " plantPort=" + plantPort
                    + "startDateTime=" + scheduleStartDateStr
                    + "duration=" + duration + "frequency=" + frequency);

            // Select intent based on caller activity/drawer layout
            Session session = Session.fromJson(getIntent().getStringExtra("session"));
            Intent intent = null;
            if(session.getPlantItem() != null) {
                // When parent caller is PlantInfoActivity
                intent = new Intent(SetScheduleActivity.this, PlantInfoActivity.class);
            } else {
                // When caller is drawer
                intent = new Intent(SetScheduleActivity.this, DevicesListActivity.class);
            }

            // Start next session
            startNextActivity(TAG_NAME, intent, session);
        } catch (Exception e) {
            resultMessage = "Error occurred while creating a schedule !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

}
