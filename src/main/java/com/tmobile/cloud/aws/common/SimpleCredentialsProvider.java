package com.tmobile.cloud.aws.common;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

public class SimpleCredentialsProvider implements AWSCredentialsProvider
{
    private final String access;
    private final String secret;

    public SimpleCredentialsProvider(final String access, final String secret)
    {
        this.access = access;
        this.secret = secret;
    }

    @Override
    public AWSCredentials getCredentials()
    {
        return new BasicAWSCredentials(access, secret);
    }

    @Override
    public void refresh()
    {
        //empty
    }
}