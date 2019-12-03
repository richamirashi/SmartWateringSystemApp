package com.sws.app.db.model;

public enum PlantPort {
    PLANT_PORT_1,
    PLANT_PORT_2;

    public static PlantPort parsePlantPort(String port) {
        if("Plant 1".equals(port)) {
            return PLANT_PORT_1;
        } else if("Plant 2".equals(port)) {
            return PLANT_PORT_2;
        } else {
            throw new IllegalArgumentException("Unsupported plant port, " + port);
        }
    }
}
