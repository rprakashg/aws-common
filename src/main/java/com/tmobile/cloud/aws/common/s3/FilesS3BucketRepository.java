package com.tmobile.cloud.aws.common.s3;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;

import java.io.*;

public class FilesS3BucketRepository extends S3BucketRepositoryImpl<byte[]> {
    private final String contentType;

    public FilesS3BucketRepository(final String bucketName, final String contentType, final Boolean makePublic){
        super(bucketName, makePublic);
        this.contentType = contentType;
    }

    @Override
    protected byte[] readObject(S3Object object) throws IOException {
        try(InputStream inputStream = object.getObjectContent()) {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        }
    }

    @Override
    protected void writeObject(String key, byte[] entity) {
        long contentLength = entity.length;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        metadata.setContentType(this.contentType);

        ByteArrayInputStream bais = new ByteArrayInputStream(entity);
        PutObjectRequest por = new PutObjectRequest(this.bucketName, key, bais, metadata);
        amazonS3Client.putObject(por);
    }
}
