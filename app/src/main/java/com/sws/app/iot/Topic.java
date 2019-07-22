package com.sws.app.iot;

/**
 * Class used to manage IOT topics
 */
public final class Topic {

    public static final String WATER_PLANT_TOPIC = "/sws/%s/%s/waterplant";
    public static final String CREATE_SCHEDULE_TOPIC = "/sws/%s/%s/createschedule";
    public static final String MOISTURE_STATS_TOPIC = "/sws/%s/%s/moisturestats";

    public static final String REQUEST = "request";
    public static final String RESPONSE = "response";

    /**
     * Don't let anyone instantiate this class.
     */
    private Topic() {}

    public static String getWaterPlantTopic(String deviceId, String type){
        return String.format(WATER_PLANT_TOPIC, deviceId, type);
    }

    public static String createScheduleTopic(String deviceId, String type){
        return String.format(CREATE_SCHEDULE_TOPIC, deviceId, type);
    }

    public static String moistureStatsTopic(String deviceId, String type){
        return String.format(MOISTURE_STATS_TOPIC, deviceId, type);
    }

}
