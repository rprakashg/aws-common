package com.tmobile.cloud.aws.common.sqs;

import com.google.gson.annotations.Expose;

public abstract class SqsMessageBase {
    @Expose(serialize = false, deserialize = false)
    private String receiptHandle = null;

    public String getReceiptHandle() {
        return receiptHandle;
    }

    public void setReceiptHandle(String receiptHandle) {
        this.receiptHandle = receiptHandle;
    }
}
