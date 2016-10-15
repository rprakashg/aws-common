package com.tmobile.cloud.aws.common.sqs;

public abstract class SqsMessageBase {
    private String receiptHandle = null;

    public String getReceiptHandle() {
        return receiptHandle;
    }

    public void setReceiptHandle(String receiptHandle) {
        this.receiptHandle = receiptHandle;
    }
}
