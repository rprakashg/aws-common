package com.tmobile.cloud.aws.common.sqs;

public class SqsQueueEmptyException extends Exception {

    public SqsQueueEmptyException(String message){
        super(message);
    }
}
