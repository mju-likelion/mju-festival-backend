package org.mju_likelion.festival.image.util.s3;

import static org.mju_likelion.festival.common.exception.type.ErrorType.FILE_EMPTY_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.FILE_NOT_IMAGE_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.FILE_SIZE_EXCEED_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.IMAGE_UPLOAD_ERROR;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.common.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * S3 에 이미지를 저장하는 서비스
 */
@Service
@RequiredArgsConstructor
public class S3ImageService {

  private final AmazonS3 amazonS3;
  private final int MB = 1024 * 1024;
  private final int MAX_FILE_SIZE = MB * 10;
  @Value("${cloud.aws.s3.bucket}")
  private String bucket;
  @Value("${cloud.aws.cloudfront.url}")
  private String cloudfrontUrl;

  /**
   * 이미지를 S3 에 저장하고 이미지 URL 을 반환한다. 이미지가 저장되는 위치는 ImageType 에 따라 달라진다.
   *
   * @param image     이미지 파일
   * @param imageType 이미지 타입
   * @return 이미지 URL
   */
  public String saveImage(MultipartFile image, ImageType imageType) {
    try {
      validate(image);

      String filename = makeFileName(imageType);

      ObjectMetadata metadata = createObjectMetadata(image);

      amazonS3.putObject(bucket, filename, image.getInputStream(), metadata);
      return getCloudfrontUrl(filename);
    } catch (Exception e) {
      throw new InternalServerException(IMAGE_UPLOAD_ERROR, e.getMessage());
    }
  }

  /**
   * 이미지 파일 이름을 생성한다.
   *
   * @param imageType 이미지 타입
   * @return 이미지 파일 이름
   */
  private String makeFileName(ImageType imageType) {
    return imageType.getLocation() + UUID.randomUUID();
  }

  /**
   * 이미지 파일의 메타데이터를 생성한다.
   *
   * @param image 이미지 파일
   * @return 이미지 파일의 메타데이터
   */
  private ObjectMetadata createObjectMetadata(MultipartFile image) {
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(image.getSize());
    metadata.setContentType(image.getContentType());
    return metadata;
  }

  /**
   * Cloudfront URL 을 생성한다.
   *
   * @param filename 파일 이름
   * @return Cloudfront URL
   */
  private String getCloudfrontUrl(String filename) {
    return cloudfrontUrl.concat(filename);
  }

  /**
   * 이미지 파일을 검증한다.
   *
   * @param image 이미지 파일
   */
  private void validate(MultipartFile image) {
    validateEmptyFile(image);
    validateImageExtension(image);
    validateImageSize(image);
  }

  /**
   * 파일이 비어있는지 검증한다.
   *
   * @param multipartFile 이미지 파일
   */
  private void validateEmptyFile(MultipartFile multipartFile) {
    if (multipartFile == null
        || multipartFile.isEmpty()
        || multipartFile.getSize() == 0
        || multipartFile.getOriginalFilename() == null
        || multipartFile.getOriginalFilename().isEmpty()) {
      throw new BadRequestException(FILE_EMPTY_ERROR);
    }
  }

  /**
   * 이미지 파일의 확장자가 이미지인지 검증한다.
   *
   * @param image 이미지 파일
   */
  private void validateImageExtension(MultipartFile image) {
    String contentType = image.getContentType();
    if (!contentType.startsWith("image/")) {
      throw new BadRequestException(FILE_NOT_IMAGE_ERROR);
    }
  }

  /**
   * 이미지 파일의 크기가 10MB 를 넘는지 검증한다.
   *
   * @param image 이미지 파일
   */
  private void validateImageSize(MultipartFile image) {
    if (image.getSize() > MAX_FILE_SIZE) {
      throw new BadRequestException(FILE_SIZE_EXCEED_ERROR);
    }
  }
}
