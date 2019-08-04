package com.sws.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sws.app.R;
import com.sws.app.commons.Session;
import com.sws.app.db.DDBManager;

public class DeviceRegistrationActivity extends AppCompatActivity {

    private static final String TAG_NAME = "DeviceRegstrationActvty";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "DeviceRegistrationActivity.onCreate called ");
        setContentView(R.layout.device_registration);

        Button registerButton = (Button) this.findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "Inside listener function for deviceRegister button");
                registerDevice();
            }
        });

        Button cancelButton = (Button)this.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "onClick: inside listener function for cancel");
                Intent intent = new Intent(DeviceRegistrationActivity.this, DevicesListActivity.class);
                Session session = Session.fromJson(getIntent().getStringExtra("session"));
                Log.i(TAG_NAME, "Cancel Session: " + session.toJson());
                intent.putExtra("session", session.toJson());
                startActivity(intent);
            }
        });
    }

    private void registerDevice(){
        String resultMessage;
        Session session = Session.fromJson(getIntent().getStringExtra("session"));
        Log.i(TAG_NAME, "registerDevice Session: " + session.toJson());
        String username = session.getUsername();
        String deviceId = ((EditText)findViewById(R.id.text_device_id)).getText().toString();
        String deviceName = ((EditText)findViewById(R.id.text_device_name)).getText().toString();

        // validate input
        if(deviceId == null || deviceId.isEmpty()) {
            resultMessage = "DeviceId cannot be empty !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG_NAME, "deviceId=" + deviceId + " | deviceName=" + deviceName );

        try {
            DDBManager ddbManager = DDBManager.getInstance();
            ddbManager.registerDevice(username, deviceId, deviceName);
            resultMessage = "Device registered !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DeviceRegistrationActivity.this, DevicesListActivity.class);
            intent.putExtra("session", session.toJson());
            startActivity(intent);
        } catch (DDBManager.DeviceAlreadyExistsException e) {
            resultMessage = "Device already exists !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            resultMessage = "Error occurred during device registration !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}