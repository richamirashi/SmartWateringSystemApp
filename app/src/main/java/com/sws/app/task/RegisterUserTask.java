package com.sws.app.task;

import android.os.AsyncTask;
import android.util.Log;

import com.sws.app.db.DDBManager;

public class RegisterUserTask extends AsyncTask<String, Void, Void> {

    private static final String TAG_NAME = "RegisterUserTask";

    private String resultMessage;

    private boolean isSuccessful;

    @Override
    protected Void doInBackground(String... strings) {
        Log.i(TAG_NAME, "RegisterUserTask.doInBackground called ");
        int count = strings.length;
        if (count != 2) {
            resultMessage = "RegisterUserTask.doInBackground invalid parameter count";
            Log.i(TAG_NAME, resultMessage);
            return null;
        }

        String username = strings[0];
        String password = strings[1];

        try {
            DDBManager ddbManager = DDBManager.getInstance();
            ddbManager.registerUser(username, password);
        } catch (DDBManager.UserCreationException e) {
            resultMessage = "Error during user registration !";
            return null;
        } catch (DDBManager.UserAlreadyExistsException e) {
            resultMessage = "User already exists !";
            return null;
        }

        resultMessage = "Login Successful !";
        isSuccessful = true;

        return null;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }


}
