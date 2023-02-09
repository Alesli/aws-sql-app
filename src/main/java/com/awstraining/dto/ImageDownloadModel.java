package com.awstraining.dto;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ImageDownloadModel {

    private long id;
    private String name;
    private long size;
    private String fileExtension;
    private ObjectMetadata objectMetadata;
    private LocalDateTime lastUpdateDate;
    private byte[] bitmap;
}
