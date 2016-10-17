package com.rprakashg.cloud.aws.common.s3;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.rprakashg.cloud.aws.common.JsonSerializer;

import java.io.*;

public class EntitiesS3BucketRepository<T> extends S3BucketRepositoryImpl<T> {
    private final Class<T> clazz;

    public EntitiesS3BucketRepository(final Class<T> clazz, final String bucketName, final Boolean makePublic){
        super(bucketName, makePublic);
        this.clazz = clazz;
    }

    @Override
    protected T readObject(S3Object object) throws IOException {
        StringBuilder contents = new StringBuilder();

        try(InputStream inputStream = object.getObjectContent()) {
            try(BufferedReader reader = new BufferedReader(new
                    InputStreamReader(inputStream))){
                String line = reader.readLine();
                while(line != null){
                    contents.append(line);
                    line = reader.readLine();
                }
            }
        }
        JsonSerializer<T> serializer = new JsonSerializer<>(this.clazz);
        return serializer.fromJson(contents.toString());
    }

    @Override
    protected void writeObject(String key, T entity) {
        JsonSerializer<T> serializer = new JsonSerializer<>(this.clazz);
        String json = serializer.toJson(entity);
        byte[] buffer = json.getBytes();
        long contentLength = buffer.length;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);
        metadata.setContentType("application/json");
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        PutObjectRequest por = new PutObjectRequest(this.bucketName, key, bais, metadata);
        amazonS3Client.putObject(por);
    }
}
