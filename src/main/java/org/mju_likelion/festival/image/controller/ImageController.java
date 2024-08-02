package org.mju_likelion.festival.image.controller;

import lombok.AllArgsConstructor;
import org.mju_likelion.festival.image.service.ImageService;
import org.mju_likelion.festival.image.util.s3.ImageType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/images")
public class ImageController {

  private final ImageService imageService;

  @PostMapping
  public ResponseEntity<Void> uploadImage(
      @RequestPart MultipartFile image,
      @RequestParam ImageType type) {
    imageService.saveImage(image, type);
    return ResponseEntity.ok().build();
  }
}
