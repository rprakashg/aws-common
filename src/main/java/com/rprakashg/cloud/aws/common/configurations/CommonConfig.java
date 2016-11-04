package com.rprakashg.cloud.aws.common.configurations;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.rprakashg.cloud.aws.common.properties.AwsProperties;
import com.rprakashg.cloud.aws.common.AwsUtils;
import com.rprakashg.cloud.aws.common.properties.AwsCredentialsProperties;
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

    @Bean
    public AmazonS3Client getS3Client(){
        final AWSCredentialsProvider credentials = AwsUtils.createCredentialsProvider(awsCredentialsProperties.getKeyId(),
                awsCredentialsProperties.getSecret());
        final AmazonS3Client amazonS3Client = new AmazonS3Client(credentials);
        final Region region = Region.getRegion(Regions.fromName(awsProperties.getRegion()));
        amazonS3Client.setRegion(region);

        return amazonS3Client;
    }

    @Bean
    public AmazonDynamoDBClient getDynamoDbClient(){
        final AWSCredentialsProvider credentials = AwsUtils.createCredentialsProvider(awsCredentialsProperties.getKeyId(),
                awsCredentialsProperties.getSecret());
        final AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentials);
        final Region region = Region.getRegion(Regions.fromName(awsProperties.getRegion()));
        dynamoDBClient.setRegion(region);
        return dynamoDBClient;
    }
}
