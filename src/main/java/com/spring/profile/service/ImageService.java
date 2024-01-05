package com.spring.profile.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spring.profile.entity.Image;
import com.spring.profile.repository.ImageRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public void save(Image image, MultipartFile file) {
    try {
      image.setImageData(file.getBytes());
      imageRepository.save(image);
    } catch (Exception e) {
      log.debug("Some internal error occurred", e);
    }
  }

  public List<Image> getAll() {
    return imageRepository.findAll();
  }

  public Optional<Image> findById(Long imageId) {
    return imageRepository.findById(imageId);
  }
}
