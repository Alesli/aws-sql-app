package com.awstraining.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;

@Service
public class S3Service {

    private static final String S3_BUCKET_NAME = "alesia-skarakhod-web-site/images";

    @Autowired
    private AmazonS3 s3;

    public byte[] downloadImage(String imageName) {
        doesBucketExist();
        S3Object object = s3.getObject(S3_BUCKET_NAME, imageName);
        try {
            return object.getObjectContent().readAllBytes();
        } catch (IOException | AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    public void deleteImage(String objectName) {
        doesBucketExist();
        s3.deleteObject(S3_BUCKET_NAME, objectName);
    }

    private void doesBucketExist() {
        if (!s3.doesBucketExistV2(S3_BUCKET_NAME)) {
            s3.createBucket(S3_BUCKET_NAME);
        }
    }
}
