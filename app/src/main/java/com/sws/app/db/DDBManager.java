package com.sws.app.db;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.regions.Region;
import com.sws.app.commons.HashUtils;
import com.sws.app.db.model.UserItem;

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

        if(loadedUserItem.getPassword().equals(HashUtils.getHash(password))){
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
}
