package com.rprakashg.cloud.aws.common.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import static java.util.stream.Collectors.toList;

public abstract class S3BucketRepositoryImpl<T> implements S3BucketRepository<T> {
    private static final Logger LOG = LoggerFactory.getLogger(S3BucketRepositoryImpl.class);

    protected final String bucketName;
    protected final Boolean makePublic;

    @Autowired
    protected AmazonS3Client amazonS3Client;

    public S3BucketRepositoryImpl(final String bucketName, final Boolean makePublic) {
        this.bucketName = bucketName;
        this.makePublic = makePublic;
    }

    @Override
    public void ensureExists() {
        if(!amazonS3Client.doesBucketExist(this.bucketName)) {
            try{
                LOG.info("Creating bucket : {}", this.bucketName);
                CreateBucketRequest cbr = new CreateBucketRequest(this.bucketName);
                if(this.makePublic)
                    LOG.warn("Making bucket public feature is not implemented");
                amazonS3Client.createBucket(cbr);
            } catch (AmazonServiceException ase) {
                LOG.error("Service Error", ase);
            } catch (AmazonClientException ace) {
                LOG.error("Error talking to S3", ace);
            }
        }
    }

    @Override
    public String getUrl(final String key) {
        LOG.info("Getting object URL for key {}", key);
        return amazonS3Client.getResourceUrl(this.bucketName, key);
    }

    @Override
    public T get(final String key){
        try{
            GetObjectRequest gor = new GetObjectRequest(this.bucketName, key);
            S3Object object = amazonS3Client.getObject(gor);
            return readObject(object);
        } catch(AmazonServiceException ase){
            LOG.error("S3 Service exception occurred: ", ase);
            throw ase;
        } catch(AmazonClientException ace){
            LOG.error("Error connecting to S3 :", ace);
            throw ace;
        } catch(IOException e){
            LOG.error("IO Exception caught :", e);
        }
        return null;
    }

    @Override
    public List<T> listItems(){
        try{
            ListObjectsRequest lor = new ListObjectsRequest()
                                            .withBucketName(this.bucketName);
            ObjectListing listing =  amazonS3Client.listObjects(lor);
            List<S3ObjectSummary> summaries = listing.getObjectSummaries();

            return summaries
                        .stream()
                        .map(s -> get(s.getKey()))
                        .collect(toList());

        } catch(AmazonServiceException ase){
            LOG.error("S3 service exception occurred: ", ase);
            throw ase;
        } catch(AmazonClientException ace) {
            LOG.error("Error connecting to S3: ", ace);
            throw ace;
        }
    }

    @Override
    public void save(final String key, T item) {
        LOG.info("Saving {} to S3 bucket {}", key, this.bucketName);
        writeObject(key, item);
        LOG.info("Done saving {} to S3", key);
    }

    @Override
    public void delete(final String key) {
        LOG.info("Deleting {} from S3 bucket {}", key, this.bucketName);
        amazonS3Client.deleteObject(this.bucketName, key);
    }

    protected abstract T readObject(S3Object object) throws IOException;
    protected abstract void writeObject(String key, T entity);
}
