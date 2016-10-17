package com.rprakashg.cloud.aws.common.sqs;


import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqsRepository<T extends SqsMessageBase> {
    String getQueueUrl();
    void ensureExists();
    void clear();
    T getMessage() throws SqsQueueEmptyException;
    List<T> getMessages(int maxMessages) throws SqsQueueEmptyException;
    void addMessage(T message);
    void deleteMessage(T message);

}
