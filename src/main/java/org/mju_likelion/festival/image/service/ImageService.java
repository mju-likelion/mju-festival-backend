package org.mju_likelion.festival.image.service;

import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.image.domain.Image;
import org.mju_likelion.festival.image.domain.repository.ImageJpaRepository;
import org.mju_likelion.festival.image.dto.response.ImageResponse;
import org.mju_likelion.festival.image.util.s3.ImageType;
import org.mju_likelion.festival.image.util.s3.S3ImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

  private final S3ImageService s3ImageService;
  private final ImageJpaRepository imageJpaRepository;

  public ImageResponse saveImage(final MultipartFile image, final ImageType type) {
    String url = s3ImageService.saveImage(image, type);

    Image newImage = new Image(url);
    imageJpaRepository.save(newImage);

    return ImageResponse.from(newImage);
  }
}
