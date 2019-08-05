package com.sws.app.iot.message;

import com.google.gson.Gson;

public class GetSoilMoistureStatsRequest extends Request {

    public GetSoilMoistureStatsRequest(String plantPort) {
        super(plantPort);
    }

    public String toJson() {
        return (new Gson()).toJson(this);
    }

    public static GetSoilMoistureStatsRequest fromJson(String json) {
        return (new Gson()).fromJson(json, GetSoilMoistureStatsRequest.class);
    }
}
