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
import android.widget.TextView;
import android.widget.Toast;

import com.sws.app.R;
import com.sws.app.adapter.PlantAdapter;
import com.sws.app.commons.Session;
import com.sws.app.db.DDBManager;
import com.sws.app.db.model.PlantItem;
import com.sws.app.listener.ItemClickListener;

import java.util.List;

public class PlantsListActivity extends BaseActivity implements ItemClickListener {

    private static final String TAG_NAME = "PlantsListActivity";

    PlantAdapter plantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "PlantsListActivity.onCreate called ");

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.plants_list, contentFrameLayout);

        Session session = Session.fromJson(getIntent().getStringExtra("session"));

        TextView deviceIDTextView = findViewById(R.id.tv_deviceid);
        deviceIDTextView.setText(session.getDeviceItem().getDeviceId());

        TextView deviceNameTextView = findViewById(R.id.tv_device_name);
        deviceNameTextView.setText(session.getDeviceItem().getDeviceName());

        Button registerButton = (Button) this.findViewById(R.id.button_plant_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "Inside listener function for Register Plant button");
                Intent intent = new Intent(PlantsListActivity.this, PlantRegistrationActivity.class);
                Session session = Session.fromJson(getIntent().getStringExtra("session"));
                Log.i(TAG_NAME, "Register Plant Session: " + session.toJson());
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

        Intent intent = new Intent(PlantsListActivity.this, DevicesListActivity.class);
        Session session = Session.fromJson(getIntent().getStringExtra("session"));
        Log.i(TAG_NAME, "Back button: " + session.toJson());
        intent.putExtra("session", session.toJson());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Session session = Session.fromJson(getIntent().getStringExtra("session"));
        String deviceId = session.getDeviceItem().getDeviceId();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_plants);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // specify an adapter (see also next example)
        try {
            DDBManager ddbManager = DDBManager.getInstance();
            List<PlantItem> plantItemList = ddbManager.listPlants(deviceId);
            plantAdapter = new PlantAdapter(plantItemList);
            plantAdapter.setClickListener(this);
            recyclerView.setAdapter(plantAdapter);
        } catch (DDBManager.PlantListException e) {
            String resultMessage = "Error occurred while fetching the plant list !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Session session = Session.fromJson(getIntent().getStringExtra("session"));
        Intent intent = new Intent(PlantsListActivity.this, PlantInfoActivity.class);
        Session newSession = new Session(session.getUsername(), session.getDeviceItem(), plantAdapter.getPlant(position));
        Log.i(TAG_NAME, "On Device click Session: " + newSession.toJson());
        intent.putExtra("session", newSession.toJson());
        startActivity(intent);
    }
}
