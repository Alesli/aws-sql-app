package com.awstraining.service;

import com.awstraining.dto.ImageModelDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    void upload(MultipartFile file);

    byte[] download(String name);

    List<ImageModelDto> findAllMetadata();

    void deleteByName(String name);

    ImageModelDto getOneRandomMetadata();
}
