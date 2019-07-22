package com.sws.app.iot.message;

public abstract class Request {

    private String plantPort;

    public Request(String plantPort){
        this.plantPort = plantPort;
    }

    public String getPlantPort() {
        return plantPort;
    }

    public abstract String toJson();
}
