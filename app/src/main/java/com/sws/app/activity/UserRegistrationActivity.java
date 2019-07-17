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
import com.sws.app.db.DDBManager;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final String TAG_NAME = "UserRegistrationActivty";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "UserRegistrationActivity.onCreate called ");
        setContentView(R.layout.user_registration);

        Button registerButton = (Button) this.findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "Inside listener function for userRegister button");
                registerUser();
            }
        });

        Button cancelButton = (Button)this.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "onClick: inside listener function for cancel");
                Intent intent = new Intent(UserRegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser(){
        String resultMessage;
        String username = ((EditText)findViewById(R.id.text_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.text_password)).getText().toString();

        // validate input
        if(username == null || username.isEmpty() || password == null || password.isEmpty()) {
            resultMessage = "Username or password cannot be empty !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(TAG_NAME, "Username=" + username + " | password=" + password );

        try {
            DDBManager ddbManager = DDBManager.getInstance();
            ddbManager.registerUser(username, password);
            resultMessage = "User registered !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UserRegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        } catch (DDBManager.UserAlreadyExistsException e) {
            resultMessage = "User already exists !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            resultMessage = "Error occurred during user registration !";
            Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
