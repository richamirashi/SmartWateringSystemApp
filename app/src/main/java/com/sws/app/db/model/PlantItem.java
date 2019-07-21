package com.sws.app.db.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "plant")
public class PlantItem {

    private String deviceId;
    private String plantPort;
    private String plantName;
    private String type;
    private String description;
    private String moistureStat;
    private String lastWatered;
    private String lastScheduled;

    @DynamoDBHashKey(attributeName = "deviceId")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @DynamoDBRangeKey(attributeName = "plantPort")
    public String getPlantPort() {
        return plantPort;
    }

    public void setPlantPort(String plantPort) {
        this.plantPort = plantPort;
    }

    @DynamoDBAttribute(attributeName = "plantName")
    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    @DynamoDBAttribute(attributeName = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "moistureStat")
    public String getMoistureStat() {
        return moistureStat;
    }

    public void setMoistureStat(String moistureStat) {
        this.moistureStat = moistureStat;
    }

    @DynamoDBAttribute(attributeName = "lastWatered")
    public String getLastWatered() {
        return lastWatered;
    }

    public void setLastWatered(String lastWatered) {
        this.lastWatered = lastWatered;
    }

    @DynamoDBAttribute(attributeName = "lastScheduled")
    public String getLastScheduled() {
        return lastScheduled;
    }

    public void setLastScheduled(String lastScheduled) {
        this.lastScheduled = lastScheduled;
    }
}
