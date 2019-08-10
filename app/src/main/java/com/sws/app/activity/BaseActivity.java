package com.sws.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.sws.app.R;
import com.sws.app.commons.Session;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SESSION_NAME = "session";

//    protected Session currentSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar listener
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

//    protected void setNavBarUserName(String userName) {
//        TextView username = findViewById(R.id.tv_username);
//        username.setText(userName);
//    }

    protected void startNextActivity(String tag, Intent intent, Session session) {
        intent.putExtra(SESSION_NAME, session.toJson());
        Log.i(tag, "Session: " + session.toJson());
        startActivity(intent);
    }

    /**
     * On navigation view item selected.
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String TAG_NAME = "NavigationItemSelected";
        Session session = Session.fromJson(getIntent().getStringExtra("session"));

        if (id == R.id.nav_devices) {
            Log.i(TAG_NAME, "Inside nav menu for Device List option");
            Intent intent = new Intent(this, DevicesListActivity.class);
            Session nextSession = new Session(session.getUsername());
            startNextActivity(TAG_NAME, intent, nextSession);
        } else if (id == R.id.nav_water_plant) {
            Log.i(TAG_NAME, "Inside nav menu for Water Plant option");
            Intent intent = new Intent(this, WaterPlantActivity.class);
            Session nextSession = new Session(session.getUsername());
            startNextActivity(TAG_NAME, intent, nextSession);
        } else if (id == R.id.nav_set_schedule) {
            Log.i(TAG_NAME, "Inside nav menu for Set Schedule option");
            Intent intent = new Intent(this, SetScheduleActivity.class);
            Session nextSession = new Session(session.getUsername());
            startNextActivity(TAG_NAME, intent, nextSession);
        } else if (id == R.id.nav_soil_moisture_stats) {
            Log.i(TAG_NAME, "Inside nav menu for Soil Moisture Stats option");
            Intent intent = new Intent(this, SoilMoistureStatsActivity.class);
            Session nextSession = new Session(session.getUsername());
            startNextActivity(TAG_NAME, intent, nextSession);
        } else if (id == R.id.nav_signout) {
            Log.i(TAG_NAME, "Inside nav menu for SignOut option");
            Intent intent = new Intent(this, LoginActivity.class);
            //Session nextSession = null;
            //startNextActivity(TAG_NAME, intent, null);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
