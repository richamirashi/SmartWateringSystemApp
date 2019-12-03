package com.sws.app.iot.message;

import com.sws.app.db.model.PlantPort;

public abstract class Request {

    private String plantPort;

    public Request(PlantPort plantPort){
        this.plantPort = plantPort.name();
    }

    public String getPlantPort() {
        return plantPort;
    }

    public abstract String toJson();
}
