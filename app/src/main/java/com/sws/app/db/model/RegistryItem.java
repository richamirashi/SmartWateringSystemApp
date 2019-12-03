package com.sws.app.db.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "registry")
public class RegistryItem {

    private String deviceId;

    private String username;

    @DynamoDBHashKey(attributeName = "deviceId")
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @DynamoDBAttribute(attributeName = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        // If the object is compared with itself then return true
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof RegistryItem)) {
            return false;
        }

        RegistryItem deviceItemObj = (RegistryItem) obj;

        return this.getDeviceId().equals(deviceItemObj.getDeviceId());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((deviceId == null) ? 0 : deviceId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return deviceId;
    }
}
