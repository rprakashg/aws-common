package com.rprakashg.cloud.aws.common.s3;

import com.sun.jndi.toolkit.url.Uri;

import java.io.IOException;
import java.util.List;

public interface S3BucketRepository<T> {

    void ensureExists();
    String getUrl(final String key);
    T get(final String key);
    List<T> listItems();
    void save(final String key, T item);
    void delete(final String key);
}
