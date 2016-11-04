/*package com.rprakashg.cloud.aws.common.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamoDbRepositoryImpl<T> implements DynamoDbRepository<T> {
    private static final Logger LOG = LoggerFactory.getLogger(DynamoDbRepositoryImpl.class);

    private final String tableName;

    @Autowired
    private  AmazonDynamoDBClient dynamoDBClient;

    public DynamoDbRepositoryImpl(final String tableName){
        this.tableName = tableName;
    }

    @Override
    public void ensureExists(String primaryKey, long readCapacityUnits, long writeCapacityUnits) {
        LOG.info("Primary key {}", primaryKey);
        ArrayList<KeySchemaElement> keySchemaElements = new ArrayList<>();
        keySchemaElements.add(new KeySchemaElement()
            .withAttributeName(primaryKey)
            .withKeyType(KeyType.HASH)
        );
        CreateTableRequest ctr = new CreateTableRequest()
                .withTableName(this.tableName)
                .withKeySchema(keySchemaElements)
                .withProvisionedThroughput(new ProvisionedThroughput()
                    .withReadCapacityUnits(readCapacityUnits)
                    .withWriteCapacityUnits(writeCapacityUnits)
                );
        LOG.info("Creating DynamoDB table {}", this.tableName);
        DynamoDB db = new DynamoDB(dynamoDBClient);
        Table table = db.createTable(ctr);
        LOG.info("Waiting for {} to be created...this may take a while...", this.tableName);
        try {
            table.waitForActive();
        }catch (InterruptedException e){
            LOG.error("Create table {} failed ", this.tableName, e);
        }
    }

    @Override
    public List<T> findAll(Map<String, String> attributes) {
        return null;
    }

    @Override
    public T findOne(String primaryKey) {
        return null;
    }

    @Override
    public T save(T entity) {
        return null;
    }

    @Override
    public void delete(T entity) {

    }
}*/
