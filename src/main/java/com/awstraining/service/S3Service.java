package com.awstraining.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class S3Service {

    private static final String S3_BUCKET_NAME = "alesia-skarakhod-web-site/images";

    @Autowired
    private AmazonS3 s3;

    @Autowired
    private UtilityService utilityService;

    public byte[] downloadImage(String imageName) {
        doesBucketExist();

        S3Object object = s3.getObject(S3_BUCKET_NAME, imageName);
        return utilityService.readFileByName(object);
    }

    public ObjectMetadata getObjectMetadata(String imageName) {
        doesBucketExist();
        S3Object object = s3.getObject(S3_BUCKET_NAME, imageName);
        return object.getObjectMetadata();
    }

    public void uploadImage(InputStream input, String filename, String filePath) {
        doesBucketExist();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("Name", filename);
        metadata.setContentType("image/jpg");
        PutObjectRequest request = new PutObjectRequest(S3_BUCKET_NAME, filePath, input, metadata);
        request.setMetadata(metadata);
        s3.putObject(request);
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
