package org.mju_likelion.festival.image.controller;

import static org.mju_likelion.festival.common.api.ApiPaths.UPLOAD_IMAGE;

import lombok.AllArgsConstructor;
import org.mju_likelion.festival.image.dto.response.ImageResponse;
import org.mju_likelion.festival.image.service.ImageService;
import org.mju_likelion.festival.image.util.s3.ImageType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class ImageController {

  private final ImageService imageService;

  @PostMapping(UPLOAD_IMAGE)
  public ResponseEntity<ImageResponse> uploadImage(
      @RequestPart final MultipartFile image,
      @RequestParam final ImageType type) {

    return ResponseEntity.ok(imageService.saveImage(image, type));
  }
}
