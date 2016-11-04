package com.rprakashg.cloud.aws.common.dynamodb;

import java.util.List;
import java.util.Map;

public interface DynamoDbRepository<T> {
    void ensureExists(String primaryKey, long readCapacityUnits, long writeCapacityUnits);
    List<T> findAll(Map<String, String> attributes);
    T findOne(final String primaryKey);
    T save(T entity);
    void delete(T entity);
}
