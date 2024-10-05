package org.mju_likelion.festival.image.util;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mju_likelion.festival.image.util.s3.Constants.MAX_FILE_SIZE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.image.util.s3.ImageType;
import org.mju_likelion.festival.image.util.s3.S3ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@DisplayName("S3ImageService")
@ApplicationTest
public class S3ImageServiceTest {

  private final String imageName = "test-image";
  private final ImageType imageType = ImageType.BOOTH;
  @Autowired
  private S3ImageService s3ImageService;

  @DisplayName("이미지를 저장한다.")
  @Test
  void testSaveImage() {
    // given
    MultipartFile image = createImage(".jpg", "image/jpeg", 1);

    // when
    String imageUrl = s3ImageService.saveImage(image, imageType);

    // then
    assertNotNull(imageUrl);

    // clean up
    s3ImageService.deleteImage(imageUrl);
  }

  @DisplayName("용량이 초과된 이미지를 저장하려고 할 때 BadRequestException 을 던진다.")
  @Test
  void testSaveImageWithOverSize() {
    // given
    MultipartFile image = createImage(".jpg", "image/jpeg", MAX_FILE_SIZE + 1);

    // when & then
    assertThrows(BadRequestException.class, () -> s3ImageService.saveImage(image, imageType));
  }

  @DisplayName("이미지가 아닌 파일을 저장하려고 할 때 BadRequestException 을 던진다.")
  @Test
  void testSaveImageWithNotImage() {
    // given
    MultipartFile image = createImage(".txt", "text/plain", 1);

    // when & then
    assertThrows(BadRequestException.class, () -> s3ImageService.saveImage(image, imageType));
  }

  @DisplayName("비어 있는 이미지를 저장하려고 할 때 BadRequestException 을 던진다.")
  @Test
  void testSaveImageWithEmptyImage() {
    // given
    MultipartFile image = createImage(".jpg", "image/jpeg", 0);

    // when & then
    assertThrows(BadRequestException.class, () -> s3ImageService.saveImage(image, imageType));
  }

  private MultipartFile createImage(String extension, String contentType, int size) {
    return new MockMultipartFile(imageName, imageName.concat(extension), contentType,
        new byte[size]);
  }
}
