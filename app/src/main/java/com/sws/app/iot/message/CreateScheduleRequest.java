package com.sws.app.iot.message;

import com.google.gson.Gson;

public class CreateScheduleRequest extends Request {

    private String scheduledDuration;
    private String scheduledFrequency;
    private String scheduledStartTime;

    public CreateScheduleRequest(String plantPort, String scheduledStartTime, String scheduledFrequency, String scheduledDuration) {
        super(plantPort);
        this.scheduledDuration = scheduledDuration;
        this.scheduledStartTime = scheduledStartTime;
        this.scheduledFrequency = getFrequency(scheduledFrequency);
    }

    private String getFrequency(String frequency) {
        if(frequency.equalsIgnoreCase("1 Day")) {
            return "1";
        } else if(frequency.equalsIgnoreCase("2 Day")) {
            return "2";
        } else if(frequency.equalsIgnoreCase("3 Day")) {
            return "3";
        } else if(frequency.equalsIgnoreCase("Weekly")) {
            return "7";
        } else {
            return "30";
        }
    }

    public String toJson() {
        return (new Gson()).toJson(this);
    }

    public static CreateScheduleRequest fromJson(String json) {
        return (new Gson()).fromJson(json, CreateScheduleRequest.class);
    }
}
