package com.awstraining.controller;

import com.awstraining.dto.ImageUploadModel;
import com.awstraining.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("list")
    public ResponseEntity<?> getAll() {
        return imageService.findAll();
    }

    @GetMapping(value = "download/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] download(@PathVariable String name) {
        return imageService.getOneByName(name);
    }

    @PostMapping("upload")
    public ResponseEntity<?> upload(@ModelAttribute ImageUploadModel uploadModel) {
        return imageService.upload(uploadModel);
    }

    @DeleteMapping("delete/{name}")
    public ResponseEntity<?> delete(@PathVariable String name) {
        return imageService.deleteByName(name);
    }

    @GetMapping("get-random")
    public ResponseEntity<?> getRandom() {
        return imageService.getOneRandom();
    }
}
