package com.tmobile.cloud.aws.common;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import org.apache.commons.lang3.StringUtils;

public class AwsUtils {
    public static AWSCredentialsProvider createCredentialsProvider(final String accessKey, final String secretKey)
    {
        if (StringUtils.isBlank(accessKey) || StringUtils.isBlank(secretKey))
        {
            return new InstanceProfileCredentialsProvider();
        }
        return new SimpleCredentialsProvider(accessKey, secretKey);
    }
}
