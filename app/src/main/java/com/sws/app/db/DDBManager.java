package com.sws.app.db;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.regions.Region;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.sws.app.commons.HashUtils;
import com.sws.app.db.model.DeviceItem;
import com.sws.app.db.model.PlantItem;
import com.sws.app.db.model.UserItem;

import java.util.List;

/**
 * Class provides functions to access dynamodb database
 */
public class DDBManager {

    private static DDBManager ddbManager;

    private AmazonDynamoDB ddbClient;

    private DynamoDBMapper ddbMapper;

    public static DDBManager getInstance() {
        if (ddbManager == null) {
            ddbManager = new DDBManager();
        }
        return ddbManager;
    }

    private DDBManager() {
        String ACCESS_KEY = "AKIAQAGZJLRFX4RFRWXG";
        String SECRET_KEY = "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";
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        AWSCredentialsProvider awsCredentialsProvider = new StaticCredentialsProvider(awsCredentials);
        ddbClient = new AmazonDynamoDBClient(awsCredentialsProvider);
        ddbClient.setRegion(Region.getRegion("us-west-2"));
        ddbMapper = DynamoDBMapper.builder().dynamoDBClient(ddbClient).build();
    }

    /**
     * User registration
     * @param username
     * @param password
     * @throws UserAlreadyExistsException
     * @throws UserCreationException
     */
    public void registerUser(String username, String password) throws UserAlreadyExistsException, UserCreationException {
        // Construct object
        UserItem userItem = new UserItem();
        userItem.setUsername(username);

        // Check if user exists
        if (ddbMapper.load(userItem) != null) {
            throw new UserAlreadyExistsException();
        }

        // add record to database
        userItem.setPassword(HashUtils.getHash(password));
        try {
            ddbMapper.save(userItem);
        } catch (Exception e) {
            throw new UserCreationException(e);
        }
    }

    public boolean authenticateUser(String username, String password) {
        // Construct object
        UserItem userItem = new UserItem();
        userItem.setUsername(username);

        // Query database and validate password
        UserItem loadedUserItem = ddbMapper.load(userItem);
        if (loadedUserItem == null || loadedUserItem.getPassword() == null) {
            return false;
        }

        if (loadedUserItem.getPassword().equals(HashUtils.getHash(password))) {
            return true;
        }
        return false;
    }

    public class UserAlreadyExistsException extends Exception {
    }

    public class UserCreationException extends Exception {
        public UserCreationException(Exception e) {
            super(e);
        }
    }

    /**
     * Device Registration handler
     *
     * @param username
     * @param deviceId
     * @param deviceName
     * @throws DeviceAlreadyExistsException
     * @throws DeviceCreationException
     */
    public void registerDevice(String username, String deviceId, String deviceName) throws DeviceAlreadyExistsException, DeviceCreationException {
        // Construct object
        DeviceItem deviceItem = new DeviceItem();
        deviceItem.setUsername(username);
        deviceItem.setDeviceId(deviceId);

        // Check if device exists
        if (ddbMapper.load(deviceItem) != null) {
            throw new DeviceAlreadyExistsException();
        }

        // add record to database
        deviceItem.setDeviceName(deviceName);
        try {
            ddbMapper.save(deviceItem);
        } catch (Exception e) {
            throw new DeviceCreationException(e);
        }
    }

    public List<DeviceItem> listDevices(String username) throws DeviceListException {
        // Construct object
        List<DeviceItem> devicesList;
        DeviceItem deviceItem = new DeviceItem();
        deviceItem.setUsername(username);
        DynamoDBQueryExpression<DeviceItem> queryExpression = new DynamoDBQueryExpression<DeviceItem>()
                .withHashKeyValues(deviceItem);

        try {
            devicesList = ddbMapper.query(DeviceItem.class, queryExpression);
        } catch (Exception e) {
            throw new DeviceListException(e);
        }

        return devicesList;
    }

    public class DeviceAlreadyExistsException extends Exception {
    }

    public class DeviceCreationException extends Exception {
        public DeviceCreationException(Exception e) {
            super(e);
        }
    }

    public class DeviceListException extends Exception {
        public DeviceListException(Exception e) {
            super(e);
        }
    }

    /**
     * Plant registration handler
     */
    public void registerPlant(String deviceId, String plantPort, String plantName, String plantType, String plantDescription)
            throws PlantAlreadyExistsException, PlantCreationException {
        // Construct object
        PlantItem plantItem = new PlantItem();
        plantItem.setDeviceId(deviceId);
        plantItem.setPlantPort(plantPort);

        // Check if plant exists
        if (ddbMapper.load(plantItem) != null) {
            throw new PlantAlreadyExistsException();
        }

        // add record to database
        plantItem.setPlantName(plantName);
        plantItem.setType(plantType);
        plantItem.setDescription(plantDescription);
        try {
            ddbMapper.save(plantItem);
        } catch (Exception e) {
            throw new PlantCreationException(e);
        }
    }

    public List<PlantItem> listPlants(String deviceId) throws PlantListException {
        // Construct object
        List<PlantItem> plantsList;
        PlantItem plantItem = new PlantItem();
        plantItem.setDeviceId(deviceId);
        DynamoDBQueryExpression<PlantItem> queryExpression = new DynamoDBQueryExpression<PlantItem>()
                .withHashKeyValues(plantItem);

        try {
            plantsList = ddbMapper.query(PlantItem.class, queryExpression);
        } catch (Exception e) {
            throw new PlantListException(e);
        }

        return plantsList;
    }

    public class PlantAlreadyExistsException extends Exception {
    }

    public class PlantCreationException extends Exception {
        public PlantCreationException(Exception e) {
            super(e);
        }
    }

    public class PlantUpdateException extends Exception {
        public PlantUpdateException(Exception e) {
            super(e);
        }
    }

    public class PlantListException extends Exception {
        public PlantListException(Exception e) {
            super(e);
        }
    }

    public class PlantDoesNotExistException extends Exception {
    }

    public void setSchedule(String deviceId, String plantPort, String startDateTime, String frequency)
        throws PlantUpdateException, PlantDoesNotExistException {

        PlantItem plantItem = new PlantItem();
        plantItem.setDeviceId(deviceId);
        plantItem.setPlantPort(plantPort);

        try {
            // get object from database
            plantItem = ddbMapper.load(plantItem);

            // Check if plant exists
            if (plantItem == null) {
                throw new PlantDoesNotExistException();
            }

            plantItem.setScheduledStartTime(startDateTime);
            plantItem.setScheduledFrequency(frequency);

            // save object to database
            ddbMapper.save(plantItem);

        } catch (PlantDoesNotExistException e) {
            throw e;
        } catch(Exception e) {
            throw new PlantUpdateException(e);
        }
    }

}
