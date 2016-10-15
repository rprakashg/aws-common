package com.tmobile.cloud.aws.common.configurations;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.tmobile.cloud.aws.common.AwsUtils;
import com.tmobile.cloud.aws.common.properties.AwsCredentialsProperties;
import com.tmobile.cloud.aws.common.properties.AwsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {
        AwsCredentialsProperties.class,
        AwsProperties.class
})
public class CommonConfig {

    @Autowired
    private AwsCredentialsProperties awsCredentialsProperties;

    @Autowired AwsProperties awsProperties;

    @Bean
    public AmazonSQSClient getSQSClient(){
        final AWSCredentialsProvider credentials = AwsUtils.createCredentialsProvider(awsCredentialsProperties.getKeyId(),
                awsCredentialsProperties.getSecret());
        final AmazonSQSClient amazonSQSClient = new AmazonSQSClient(credentials);
        final Region region = Region.getRegion(Regions.fromName(awsProperties.getRegion()));
        amazonSQSClient.setRegion(region);

        return amazonSQSClient;
    }
}
