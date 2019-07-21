package com.sws.app.commons;

import com.google.gson.Gson;
import com.sws.app.db.model.DeviceItem;
import com.sws.app.db.model.PlantItem;

/**
 * Session Object to pass information between activities.
 */
public class Session {

    private String username;

    private DeviceItem deviceItem;

    private PlantItem plantItem;

    public Session(String username, DeviceItem deviceItem, PlantItem plantItem) {
        this.username = username;
        this.deviceItem = deviceItem;
        this.plantItem = plantItem;
    }

    public Session(String username, DeviceItem deviceItem) {
        this.username = username;
        this.deviceItem = deviceItem;
    }

    public Session(String username) {
        this.username = username;
    }

    public String toJson() {
        return (new Gson()).toJson(this);
    }

    public static Session fromJson(String json) {
        return (new Gson()).fromJson(json, Session.class);
    }

    public String getUsername() {
        return username;
    }

    public DeviceItem getDeviceItem() {
        return deviceItem;
    }

    public PlantItem getPlantItem() {
        return plantItem;
    }
}
