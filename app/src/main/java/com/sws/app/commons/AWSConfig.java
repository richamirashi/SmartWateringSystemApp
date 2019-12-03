package com.sws.app.commons;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;

public class AWSConfig {

    public static final String ACCESS_KEY = "";
    public static final String SECRET_KEY = "";
    public static final String IOT_END_POINT = "";

    public static AWSCredentialsProvider getCredentialProvider() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
        return new StaticCredentialsProvider(awsCredentials);
    }
}