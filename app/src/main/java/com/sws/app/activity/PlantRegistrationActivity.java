package com.sws.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.sws.app.R;
import com.sws.app.commons.Session;
import com.sws.app.db.DDBManager;

public class PlantRegistrationActivity extends AppCompatActivity {

    private static final String TAG_NAME = "PlantRegistrationActvty";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "PlantRegistrationActivity.onCreate called ");
        setContentView(R.layout.plant_registration);

        // For plant port spinner
        Spinner portSpinner = (Spinner) findViewById(R.id.spinner_plant_port);
        ArrayAdapter<CharSequence> portAdapter = ArrayAdapter.createFromResource(this,
                R.array.plant_port_array, android.R.layout.simple_spinner_dropdown_item);
        portAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        portSpinner.setAdapter(portAdapter);

        // For plant type spinner
        Spinner plantTypeSpinner = (Spinner) findViewById(R.id.spinner_plant_type);
        ArrayAdapter<CharSequence> plantTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.plant_type_array, android.R.layout.simple_spinner_dropdown_item);
        plantTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plantTypeSpinner.setAdapter(plantTypeAdapter);

        Button registerButton = (Button) this.findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "Inside listener function for plantRegister button");
                registerPlant();
            }
        });

        Button cancelButton = (Button) this.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "onClick: inside listener function for cancel");
                Intent intent = new Intent(PlantRegistrationActivity.this, PlantsListActivity.class);
                Session session = Session.fromJson(getIntent().getStringExtra("session"));
                Log.i(TAG_NAME, "Cancel Session: " + session.toJson());
                intent.putExtra("session", session.toJson());
                startActivity(intent);
            }
        });
    }

    private void registerPlant() {
        String resultMessage;
        Session session = Session.fromJson(getIntent().getStringExtra("session"));
        Log.i(TAG_NAME, "registerPlant Session: " + session.toJson());
        String deviceId = session.getDeviceItem().getDeviceId();
        String plantName = ((EditText) findViewById(R.id.text_plant_name)).getText().toString();
        String plantDescription = ((EditText) findViewById(R.id.text_plant_description)).getText().toString();

        Spinner portSpinner = (Spinner) findViewById(R.id.spinner_plant_port);
        String plantPort = portSpinner.getSelectedItem().toString();

        Spinner plantTypeSpinner = (Spinner) findViewById(R.id.spinner_plant_type);
        String plantType = plantTypeSpinner.getSelectedItem().toString();

        Log.i(TAG_NAME, "plantName=" + plantName + " | plantDescription=" + plantDescription);
        Log.i(TAG_NAME, "plantPort=" + plantPort + " | plantType=" + plantType);

        try {
            DDBManager ddbManager = DDBManager.getInstance();
            ddbManager.registerPlant(deviceId, plantPort, plantName, plantType, plantDescription);
            resultMessage = "Plant registered !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PlantRegistrationActivity.this, PlantsListActivity.class);
            intent.putExtra("session", session.toJson());
            startActivity(intent);
        } catch (DDBManager.PlantAlreadyExistsException e) {
            resultMessage = "Plant already registered !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            resultMessage = "Error occurred during plant registration !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}