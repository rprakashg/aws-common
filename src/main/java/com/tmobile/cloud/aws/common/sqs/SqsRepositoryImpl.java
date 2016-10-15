package com.tmobile.cloud.aws.common.sqs;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;
import com.tmobile.cloud.aws.common.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static java.util.stream.Collectors.toList;
import java.util.List;

@Repository
public class SqsRepositoryImpl<T extends SqsMessageBase> implements SqsRepository<T> {
    private static final Logger LOG = LoggerFactory.getLogger(SqsRepositoryImpl.class);

    private final String queueName;
    private final Integer visibilityTimeout;

    private final Class<T> clazz;

    @Autowired
    private AmazonSQSClient amazonSQSClient;

    private String queueUrl;

    public SqsRepositoryImpl(final Class<T> clazz, final String queueName, final Integer visibilityTimeout){
        this.clazz = clazz;
        this.queueName = queueName;
        this.visibilityTimeout = visibilityTimeout;
    }

    @Override
    public String getQueueUrl() {
        return queueUrl;
    }

    @Override
    public void ensureExists() {
        LOG.info("Checking to see if SQS Queue {} exists", this.queueName);
        ListQueuesRequest lqr = new ListQueuesRequest()
                                        .withQueueNamePrefix(this.queueName);
        ListQueuesResult result = amazonSQSClient.listQueues(lqr);
        if(result.getQueueUrls().size() == 0){
            LOG.info("SQS Queue {} doesn't exist, creating", this.queueName);
            CreateQueueResult cqr = amazonSQSClient.createQueue(this.queueName);
            queueUrl = cqr.getQueueUrl();
        } else {
            queueUrl = result.getQueueUrls().get(0); //should only be one
            LOG.info("SQS Queue {} exists, nothing to do, returning", this.queueName);
        }
        LOG.info("SQS Queue URL : {}", this.getQueueUrl());
    }

    @Override
    public void clear() {
        LOG.info("Purging SQS Queue : {}", this.queueName);
        amazonSQSClient.purgeQueue(new PurgeQueueRequest()
                                            .withQueueUrl(this.queueUrl));
    }

    @Override
    public T getMessage() throws SqsQueueEmptyException {
        return this.getMessages(1).get(0);
    }

    @Override
    public List<T> getMessages(int maxMessages) throws SqsQueueEmptyException {
        LOG.info("Receiving messages from SQS Queue : {}", this.queueName);
        ReceiveMessageRequest rmr = new ReceiveMessageRequest()
                                            .withQueueUrl(this.queueUrl)
                                            .withMaxNumberOfMessages(maxMessages)
                                            .withVisibilityTimeout(this.visibilityTimeout);
        List<Message> messages = amazonSQSClient.receiveMessage(rmr).getMessages();
        LOG.info("Number of messages received from SQS queue : {}", messages.size());
        if(messages.size() == 0)
            throw new SqsQueueEmptyException(String.format("SQS Queue %s is empty", this.queueName));



        LOG.info("Returning messages as collection of T");
        return messages
                .stream()
                .map(this::deserializeMessage)
                .collect(toList());
    }

    @Override
    public void addMessage(T message) {
        LOG.info("Adding a new message to SQS Queue : {}", this.queueName);
        JsonSerializer<T> serializer = new JsonSerializer<>(this.clazz);
        String jsonMessage = serializer.toJson(message);
        LOG.info("Message after serializing to JSON : {}", jsonMessage);
        SendMessageRequest smr = new SendMessageRequest()
                                        .withQueueUrl(this.queueUrl)
                                        .withMessageBody(jsonMessage);
        amazonSQSClient.sendMessage(smr);
        LOG.info("Message added to SQS Queue : {}", this.queueName);
    }

    @Override
    public void deleteMessage(T message) {
        LOG.info("Deleting message from SQS Queue : {}", this.queueName);
        DeleteMessageRequest dmr = new DeleteMessageRequest()
                                            .withQueueUrl(this.queueUrl)
                                            .withReceiptHandle(message.getReceiptHandle());
        amazonSQSClient.deleteMessage(dmr);
        LOG.info("Done deleting");
    }

    private T deserializeMessage(Message message){
        //new up a JsonSerializer to serialize message body JSON to T
        JsonSerializer<T> serializer = new JsonSerializer<>(this.clazz);
        T instance = serializer.fromJson(message.getBody());
        instance.setReceiptHandle(message.getReceiptHandle());
        return instance;
    }
}
