package com.sws.app.iot.message;

import com.google.gson.Gson;

public class WaterPlantRequest extends Request {

    private String duration;

    public WaterPlantRequest(String plantPort, String duration) {
        super(plantPort);
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public String toJson() {
        return (new Gson()).toJson(this);
    }

    public static WaterPlantRequest fromJson(String json) {
        return (new Gson()).fromJson(json, WaterPlantRequest.class);
    }
}
