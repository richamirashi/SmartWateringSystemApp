package com.sws.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sws.app.R;
import com.sws.app.adapter.DevicesAdapter;
import com.sws.app.commons.Session;
import com.sws.app.db.DDBManager;
import com.sws.app.db.model.DeviceItem;
import com.sws.app.listener.ItemClickListener;

import java.util.List;

public class DevicesListActivity extends BaseActivity implements ItemClickListener {

    private static final String TAG_NAME = "DevicesListActivity";

    private DevicesAdapter devicesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "onCreate called");

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.devices_list, contentFrameLayout);

        Button registerButton = (Button) this.findViewById(R.id.button_device_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "Inside listener function for Register Device button");
                Intent intent = new Intent(DevicesListActivity.this, DeviceRegistrationActivity.class);
                Session session = Session.fromJson(getIntent().getStringExtra("session"));
                Log.i(TAG_NAME, "Register Device Session: " + session.toJson());
                intent.putExtra("session", session.toJson());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        // do nothing
    }

    @Override
    protected void onResume() {
        super.onResume();
        Session session = Session.fromJson(getIntent().getStringExtra("session"));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_devices);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        try {
            DDBManager ddbManager = DDBManager.getInstance();
            List<DeviceItem> deviceItemList = ddbManager.listDevices(session.getUsername());
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
        Session session = Session.fromJson(getIntent().getStringExtra("session"));
        Intent intent = new Intent(DevicesListActivity.this, PlantsListActivity.class);
        Session newSession = new Session(session.getUsername(), devicesAdapter.getDevice(position));
        Log.i(TAG_NAME, "On Device click Session: " + newSession.toJson());
        intent.putExtra("session", newSession.toJson());
        startActivity(intent);
    }
}