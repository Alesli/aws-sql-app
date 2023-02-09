package com.awstraining.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUploadModel {

    private String name;
    private MultipartFile file;
}
