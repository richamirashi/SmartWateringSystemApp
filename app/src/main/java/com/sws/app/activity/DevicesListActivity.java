package com.sws.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sws.app.R;
import com.sws.app.adapter.DevicesAdapter;
import com.sws.app.db.DDBManager;
import com.sws.app.db.model.DeviceItem;
import com.sws.app.listener.ItemClickListener;

import java.util.List;

public class DevicesListActivity extends AppCompatActivity implements ItemClickListener {

    private static final String TAG_NAME = "DevicesListActivity";

    DevicesAdapter devicesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "DevicesListActivity.onCreate called ");
        setContentView(R.layout.devices_list);

        Button registerButton = (Button) this.findViewById(R.id.button_device_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "Inside listener function for Register Device button");
                Intent intent = new Intent(DevicesListActivity.this, DeviceRegistrationActivity.class);
                String username = getIntent().getStringExtra("username");
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        String username = getIntent().getStringExtra("username");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_devices);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        try {
            DDBManager ddbManager = DDBManager.getInstance();
            List<DeviceItem> deviceItemList = ddbManager.listDevices(username);
            devicesAdapter = new DevicesAdapter(deviceItemList);
            devicesAdapter.setClickListener(this);
            recyclerView.setAdapter(devicesAdapter);
        } catch (DDBManager.DeviceListException e) {
            String resultMessage = "Error occurred while fetching the device list !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        String resultMessage = "You selected : " + devicesAdapter.getDevice(position).getDeviceName() + " | position = " + position;
        Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
    }
}
