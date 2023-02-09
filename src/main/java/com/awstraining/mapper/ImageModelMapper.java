package com.awstraining.mapper;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.awstraining.dto.ImageDownloadModel;
import com.awstraining.dto.ImageUploadModel;
import com.awstraining.entity.ImageModel;
import com.awstraining.service.S3Service;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ImageModelMapper {

    @Mapping(target = "bitmap", ignore = true)
    ImageDownloadModel downloadModel(ImageModel entityModel, @Context S3Service s3Service);

    ImageModel toEntityModel(ImageUploadModel clientModel);

    @AfterMapping
    default void setBitmapToClient(@MappingTarget ImageDownloadModel target, ImageModel source, @Context S3Service s3Service) {
        target.setBitmap(s3Service.downloadImage(source.getName()));
    }

    @AfterMapping
    default void setObjectMetadataToClient(@MappingTarget ImageDownloadModel target, ImageModel source, @Context S3Service s3Service) {
        ObjectMetadata s3Metadata = s3Service.getObjectMetadata(source.getName());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("Name", s3Metadata.getUserMetadata().get("name"));
        metadata.setContentType(s3Metadata.getContentType());
        metadata.setLastModified(s3Metadata.getLastModified());
        metadata.setContentLength(s3Metadata.getContentLength());
        target.setObjectMetadata(metadata);
    }
}
