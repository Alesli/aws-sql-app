package com.awstraining.controller;

import com.awstraining.dto.ImageModelDto;
import com.awstraining.service.impl.ImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/")
public class ImageController {

    @Autowired
    private ImageServiceImpl imageServiceImpl;

    @GetMapping("list-metadata")
    public List<ImageModelDto> getAllMetadata() {
        return imageServiceImpl.findAllMetadata();
    }

    @GetMapping(value = "download/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] download(@PathVariable String name) {
        return imageServiceImpl.download(name);
    }

    @PostMapping("upload")
    public void upload(@ModelAttribute MultipartFile multipartFile) {
        imageServiceImpl.upload(multipartFile);
    }

    @DeleteMapping("delete/{name}")
    public void delete(@PathVariable String name) {
        imageServiceImpl.deleteByName(name);
    }

    @GetMapping("get-random")
    public ImageModelDto getRandom() {
        return imageServiceImpl.getOneRandomMetadata();
    }
}
