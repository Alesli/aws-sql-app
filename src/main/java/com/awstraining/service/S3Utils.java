package com.awstraining.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
public class S3Utils {

    private static final String S3_BUCKET_NAME = "alesia-skarakhod-web-site/images";

    @Autowired
    private AmazonS3 amazonS3Client;

    public byte[] downloadImage(String imageName) {
        doesBucketExist();
        S3Object object = amazonS3Client.getObject(S3_BUCKET_NAME, imageName);
        try {
            return object.getObjectContent().readAllBytes();
        } catch (IOException | AmazonClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    public void deleteImage(String objectName) {
        doesBucketExist();
        amazonS3Client.deleteObject(S3_BUCKET_NAME, objectName);
    }

    private void doesBucketExist() {
        if (!amazonS3Client.doesBucketExistV2(S3_BUCKET_NAME)) {
            amazonS3Client.createBucket(S3_BUCKET_NAME);
        }
    }
}
