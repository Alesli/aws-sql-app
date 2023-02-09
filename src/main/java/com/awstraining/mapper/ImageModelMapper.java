package com.awstraining.mapper;

import com.awstraining.dto.ImageModelDto;
import com.awstraining.entity.ImageModel;
import com.awstraining.service.S3Service;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageModelMapper {

    @Mapping(target = "bitmap", ignore = true)
    ImageModelDto downloadModel(ImageModel entityModel, @Context S3Service s3Service);

    @AfterMapping
    default void setBitmapToDto(@MappingTarget ImageModelDto target, ImageModel source, @Context S3Service s3Service) {
        target.setBitmap(s3Service.downloadImage(source.getName()));
    }

    @Named("mapToImageDto")
    ImageModelDto convertEntityToDto(ImageModel imageModel);

    @IterableMapping(qualifiedByName = "mapToImageDto")
    List<ImageModelDto> convertEntityToDto(List<ImageModel> imageModelList);
}
