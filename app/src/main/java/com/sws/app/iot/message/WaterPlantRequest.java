package com.sws.app.iot.message;

import com.google.gson.Gson;
import com.sws.app.db.model.PlantPort;

public class WaterPlantRequest extends Request {

    private String duration;

    public WaterPlantRequest(PlantPort plantPort, String duration) {
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
