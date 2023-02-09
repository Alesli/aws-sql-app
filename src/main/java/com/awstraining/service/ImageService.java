package com.awstraining.service;

import com.awstraining.dao.ImageJpaRepository;
import com.awstraining.dto.ImageDownloadModel;
import com.awstraining.dto.ImageUploadModel;
import com.awstraining.entity.ImageModel;
import com.awstraining.mapper.ImageModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImageService {

    @Autowired
    private ImageJpaRepository imageJpaRepository;

    @Autowired
    private ImageModelMapper imageModelMapper;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UtilityService utilityService;

    @Value("${server.port}")
    private String serverPort;

    @Transactional
    public ResponseEntity<?> findAll() {
        List<ImageModel> entities = imageJpaRepository.findAll();
        List<ImageDownloadModel> clientModels = entities.stream()
                .map(entityModel -> imageModelMapper.downloadModel(entityModel, s3Service))
                .collect(Collectors.toList());
        return new ResponseEntity<>(clientModels, HttpStatus.OK);
    }

    @Transactional
    public byte[] getOneByName(String name) {
        Optional<ImageModel> byName = imageJpaRepository.getOneByName(name);
        return imageModelMapper.downloadModel(byName.get(), s3Service).getBitmap();
    }

    @Transactional
    public ResponseEntity<?> getOneRandom() {
        Optional<ImageModel> entityModel = imageJpaRepository.getOneRandom();
        ImageDownloadModel imageDownloadModel = imageModelMapper.downloadModel(entityModel.get(), s3Service);
        return new ResponseEntity<>(imageDownloadModel, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> upload(ImageUploadModel uploadModel) {
        MultipartFile file = uploadModel.getFile();
        InputStream input = utilityService.getInputStream(file);
        s3Service.uploadImage(input, file.getOriginalFilename(), uploadModel.getName());

        ImageModel entityModel = imageModelMapper.toEntityModel(uploadModel);
        entityModel.setSize(file.getSize());
        entityModel.setLastUpdateDate(LocalDateTime.now());
        entityModel.setFileExtension(utilityService.getExtension(Objects.requireNonNull(file.getOriginalFilename())));

        ImageModel save = imageJpaRepository.save(entityModel);
        ImageDownloadModel imageDownloadModel = imageModelMapper.downloadModel(save, s3Service);
        return new ResponseEntity<>(imageDownloadModel, HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> deleteByName(String name) {
        List<ImageModel> imageModelList = imageJpaRepository.findAllByName(name);
        imageModelList.stream()
                .map(ImageModel::getName)
                .forEach(s3Service::deleteImage);
        imageModelList.forEach(imageJpaRepository::delete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
