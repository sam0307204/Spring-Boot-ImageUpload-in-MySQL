package com.spring.profile.controller;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.spring.profile.response.SaveResult;
import com.spring.profile.service.ImageService;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

@RestController
@Slf4j
public class ImageController {
    
    private final ImageService imageService;

    public ImageController(ImageService imageService) {

        this.imageService = imageService;

    }

    @GetMapping("/image/db/{filename}")
    public ResponseEntity<Resource> retrieve(@PathVariable String filename){
        var image=imageService.getImage(filename);
        var body=new ByteArrayResource(image.getData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, image.getMimeType())
                .cacheControl(CacheControl.maxAge(Duration.ofSeconds(60)).cachePrivate().mustRevalidate())
                .body(body);
    }


    @PostMapping("/image/db/upload")
    public SaveResult upload(@RequestPart MultipartFile file){
        try {
            var image=imageService.save(file);
            return SaveResult.builder()
                    .error(false)
                    .filename(image.getFilename())
                    .link(createImageLink(image.getFilename()))
                    .build();
        } catch (Exception e) {
            log.error("Failed to save image",e);
            return SaveResult.builder().error(true).filename(file.getOriginalFilename()).build();
        }
    }

    @PostMapping("/images/db/upload_multi")
    public List<SaveResult> uploadMulti(@RequestPart List<MultipartFile> files) {
        return files.stream()
                .map(this::upload)
                .collect(Collectors.toList());
    }


    private String createImageLink(String filename) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/image/db/"+filename)
                .toUriString();
    }
}
