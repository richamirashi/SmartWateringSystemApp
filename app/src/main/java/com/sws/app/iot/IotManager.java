package com.sws.app.iot;

import android.util.Log;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;
import com.sws.app.iot.message.WaterPlantRequest;

/**
 * Singleton class to interact with AWS IOT
 */
public class IotManager {

    private static IotManager iotManager;

    private AWSIotMqttManager mqttManager;

    /**
     *  This client will acknowledge to the Device Gateway that messages are received
     */
    private static final AWSIotMqttQos QOS = AWSIotMqttQos.QOS1;

    private static final String LOG_TAG = "IotManager";

    private static final String END_POINT = "a2r9hrtpe5nxqx-ats.iot.us-west-2.amazonaws.com";

    private static final String clientId = "waterPlantApp";

    public static IotManager getInstance() {
        if (iotManager == null) {
            iotManager = new IotManager();
        }
        return iotManager;
    }

    private IotManager(){
        String ACCESS_KEY = "AKIAQAGZJLRFX4RFRWXG";
        String SECRET_KEY = "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";
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AWSCredentialsProvider awsCredentialsProvider = new StaticCredentialsProvider(awsCredentials);
        mqttManager = new AWSIotMqttManager(clientId, END_POINT);
        mqttManager.setKeepAlive(10);

        try {
            mqttManager.connect(awsCredentialsProvider, new AWSIotMqttClientStatusCallback(){
                @Override
                public void onStatusChanged(AWSIotMqttClientStatus status, Throwable throwable) {
                    Log.i(LOG_TAG, "IOT Connection Status = " + String.valueOf(status));
                }
            });

            /**
             * Added delay to avoid: com.amazonaws.AmazonClientException: Client is disconnected or not yet connected.
             * TODO: Fix this if possible and check if client is connected.
             */
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            // TODO: add toast message
            e.printStackTrace();
        }

    }

    public void waterPlant(String deviceId, String plantPort, String duration) {

        Log.i(LOG_TAG, "Water plant function called");

        // Send request to Iot to water plant
        WaterPlantRequest waterPlantRequest = new WaterPlantRequest(plantPort, duration);
        String waterPlantRequestJson = waterPlantRequest.toJson();
        String waterPlantTopicRequest = Topic.getWaterPlantTopic(deviceId, Topic.REQUEST);
        mqttManager.publishString(waterPlantRequestJson, waterPlantTopicRequest, QOS);
        Log.i(LOG_TAG, "Water plant request sent | Topic= " + waterPlantTopicRequest + " | Request= " + waterPlantRequestJson);

        // Wait for response

        // Update database

    }
}
