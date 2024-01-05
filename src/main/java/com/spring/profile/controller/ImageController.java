package com.spring.profile.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import org.springframework.http.MediaType;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import com.spring.profile.entity.Image;
import com.spring.profile.service.ImageService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

@RestController
@Slf4j
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {

        this.imageService = imageService;

    }

    @GetMapping("/")
    public String home() {
        return "redirect:/profiles/view";
    }

    @GetMapping(value = "/images/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadImage(@PathVariable Long imageId) {
        byte[] image = imageService.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getImageData();

        return new ByteArrayResource(image);
    }

    @GetMapping("/profiles/view")
    public ModelAndView view(Model model) {

        return new ModelAndView("view", "profiles", imageService.getAll());
    }

    @GetMapping("/profiles/add")
    public ModelAndView addProfile() {

        return new ModelAndView("addProfile", "profile", new Image());
    }

    @PostMapping(value = "/profiles/add", consumes = MULTIPART_FORM_DATA_VALUE)
  public String handleProfileAdd(Image image, @RequestPart("file") MultipartFile file) {

    log.info("handling request parts: {}", file);
    imageService.save(image, file);
    return "redirect:/profiles/view";
  }

}
