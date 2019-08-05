package com.sws.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sws.app.R;
import com.sws.app.commons.Session;
import com.sws.app.commons.Utils;
import com.sws.app.db.DDBManager;
import com.sws.app.db.model.DeviceItem;
import com.sws.app.iot.IotManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SetScheduleActivity extends AppCompatActivity {

    private static final String TAG_NAME = "SetScheduleActivity";

    private ArrayAdapter<DeviceItem> deviceNameAdapter;

    private ArrayAdapter<CharSequence> portAdapter;

    private ArrayAdapter<CharSequence> frequencyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "SetScheduleActivity.onCreate called ");
        setContentView(R.layout.set_schedule);

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
                // TODO: Fix this for going back to the activity that called it.
                Intent intent = new Intent(SetScheduleActivity.this, PlantInfoActivity.class);
                Session session = Session.fromJson(getIntent().getStringExtra("session"));
                Log.i(TAG_NAME, "Cancel Session: " + session.toJson());
                intent.putExtra("session", session.toJson());
                startActivity(intent);
            }
        });
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

        // For frequency spinner
        Spinner frequencySpinner = (Spinner) findViewById(R.id.spinner_frequency);
        frequencyAdapter = ArrayAdapter.createFromResource(this, R.array.schedule_frequency_array, android.R.layout.simple_spinner_dropdown_item);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(frequencyAdapter);

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
        } catch (Exception e) {
            resultMessage = "Error occurred while creating a schedule !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

}
