package com.awstraining.service.impl;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.awstraining.dao.ImageRepository;
import com.awstraining.dto.ImageModelDto;
import com.awstraining.entity.ImageModel;
import com.awstraining.mapper.ImageModelMapper;
import com.awstraining.service.ImageService;
import com.awstraining.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private static final String S3_BUCKET_NAME = "alesia-skarakhod-web-site/images";

    private ImageRepository imageRepository;

    private ImageModelMapper imageModelMapper;

    private S3Service s3Service;

    private final TransferManager transferManager;

    @Value("${server.port}")
    private String serverPort;

    @Transactional
    public List<ImageModelDto> findAllMetadata() {
        return imageModelMapper.convertEntityToDto(imageRepository.findAll());
    }

    @Transactional
    public ImageModelDto getOneRandomMetadata() {
        return imageModelMapper.convertEntityToDto(imageRepository.getOneRandom());
    }

    @Transactional
    public byte[] download(String name) {
        Optional<ImageModel> imageModel = imageRepository.getOneByName(name);
        return imageModelMapper.downloadModel(imageModel.get(), s3Service).getBitmap();
    }

    @Transactional
    public void upload(MultipartFile multipartFile) {
        imageRepository.getOneByName(multipartFile.getOriginalFilename())
                .ifPresent(image -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image is already present");
                });
        try {
        var upload = transferManager.upload(S3_BUCKET_NAME,
                multipartFile.getOriginalFilename(), multipartFile.getInputStream(),
                new ObjectMetadata());
        upload.waitForUploadResult();
        var imageModel = ImageModel.builder()
                .fileExtension(FilenameUtils.getExtension(multipartFile.getOriginalFilename()))
                .name(multipartFile.getOriginalFilename())
                .size(multipartFile.getSize())
                .lastUpdateDate(LocalDateTime.now())
                .build();
//            imageRepository.saveAndFlush(imageModel);
            ImageModel save = imageRepository.save(imageModel);
            imageModelMapper.downloadModel(save, s3Service);
        } catch (IOException | InterruptedException e) {
            deleteByName(multipartFile.getOriginalFilename());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteByName(String name) {
        List<ImageModel> imageModelList = imageRepository.findAllByName(name);
        imageModelList.stream()
                .map(ImageModel::getName)
                .forEach(s3Service::deleteImage);
        imageModelList.forEach(imageRepository::delete);
    }
}
