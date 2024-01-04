package com.spring.profile.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.profile.entity.Image;
import com.spring.profile.exception.ImageNotFoundException;
import com.spring.profile.repository.ImageRepository;
import com.spring.profile.service.ImageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService{

    private final  ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {

        this.imageRepository = imageRepository;

    }

    @Override
    public Image getImage(String filename) {
       return imageRepository.findByFilename(filename)
                .orElseThrow(ImageNotFoundException::new);
    }

    @Override
    public Image save(MultipartFile file) throws Exception {
        if(imageRepository.existsByFilename(file.getOriginalFilename())){
            log.info("Image {} have already existed",file.getOriginalFilename());
            return Image.builder().filename(file.getOriginalFilename()).build();
        }

        var image=Image.builder()
                .filename(file.getOriginalFilename())
                .mimeType(file.getContentType())
                .data(file.getBytes())
                .build();
        return imageRepository.save(image);
    }

    @Override
    public List<String> listFilenames() {
        return imageRepository.findAllFilenames();
    }

    @Override
    public Iterable<Image> saveAll(List<MultipartFile> files) {
        var images = files.stream()
                .filter(file -> !imageRepository.existsByFilename(file.getOriginalFilename()))
                .map(this::newImage)
                .collect(Collectors.toList());

        return imageRepository.saveAll(images);
    }

    private Image newImage(MultipartFile file) {
        try {
            return Image.builder()
                    .filename(file.getOriginalFilename())
                    .mimeType(file.getContentType())
                    .data(file.getBytes())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int delete(String filename) {
        return imageRepository.deleteByFilename(filename);
    }

    @Override
    public Image replace(String filename, MultipartFile file) throws Exception {
        var image = imageRepository.findByFilename(filename)
                .orElseThrow(ImageNotFoundException::new);

        image.setMimeType(file.getContentType());
        image.setData(file.getBytes());

        return imageRepository.save(image);
    }
    
}
