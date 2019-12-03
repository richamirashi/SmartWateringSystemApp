package com.sws.app.iot.message;

import com.google.gson.Gson;
import com.sws.app.db.model.PlantPort;

public class GetSoilMoistureStatsRequest extends Request {

    public GetSoilMoistureStatsRequest(PlantPort plantPort) {
        super(plantPort);
    }

    public String toJson() {
        return (new Gson()).toJson(this);
    }

    public static GetSoilMoistureStatsRequest fromJson(String json) {
        return (new Gson()).fromJson(json, GetSoilMoistureStatsRequest.class);
    }
}
