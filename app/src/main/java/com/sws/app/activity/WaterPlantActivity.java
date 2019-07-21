package com.sws.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sws.app.R;

public class WaterPlantActivity extends AppCompatActivity {

    private static final String TAG_NAME = "WaterPlantActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "WaterPlantActivity.onCreate called ");
        setContentView(R.layout.water_plant);
    }
}
