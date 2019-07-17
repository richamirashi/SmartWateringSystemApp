package com.sws.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sws.app.R;
import com.sws.app.db.DDBManager;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG_NAME = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG_NAME, "LoginActivity.onCreate called ");
        setContentView(R.layout.login);

        // TODO: FIX ME: enable running sync calls in same thread as main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button loginButton = (Button) this.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "Inside listener function for login button");
                // This is a blocking call
                runOnUiThread(new RegisterUserTask());
            }
        });

        TextView signUpLink = findViewById(R.id.text_signup);
        signUpLink.setText(R.string.link_signup);
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG_NAME, "Inside listener function for user signup");
                Intent intent = new Intent(LoginActivity.this, UserRegistrationActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    public class RegisterUserTask implements Runnable {

        private static final String TAG_NAME = "RegisterUserTask";

        @Override
        public void run() {
            String resultMessage;
            Log.i(TAG_NAME, "RegisterUserTask.doInBackground called ");
            String username = ((TextView) findViewById(R.id.input_email)).getText().toString();
            String password = ((TextView) findViewById(R.id.input_password)).getText().toString();

            // validate input
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                resultMessage = "Username or password cannot be empty !";
                Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
                return;
            }

            Log.i(TAG_NAME, "Username=" + username + " | password=" + password);

            try {
                DDBManager ddbManager = DDBManager.getInstance();
                boolean isAuthorizedUser = ddbManager.authenticateUser(username, password);
                if (isAuthorizedUser) {
                    resultMessage = "Login Successful !";
                    Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();

                    // TODO: Add intent to device list
                    return;
                } else {
                    resultMessage = "Username or password is incorrect !";
                    Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                resultMessage = "Error occurred during login !";
                Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }


}
