package org.mju_likelion.festival.booth.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.mju_likelion.festival.booth.domain.BoothDetail;

/**
 * 부스 상세 응답 DTO.
 */
@Getter
public class BoothDetailResponse extends SimpleBoothResponse {

  private final String location;
  private final List<String> imageUrl;
  private final LocalDateTime createdAt;

  public BoothDetailResponse(
      final UUID id,
      final String name,
      final String description,
      final String location,
      final String thumbnailUrl,
      final List<String> imageUrl,
      final LocalDateTime createdAt) {

    super(id, name, description, thumbnailUrl);
    this.location = location;
    this.imageUrl = imageUrl;
    this.createdAt = createdAt;
  }

  public static BoothDetailResponse from(final BoothDetail boothDetail) {
    return new BoothDetailResponse(boothDetail.getId(), boothDetail.getName(),
        boothDetail.getDescription(), boothDetail.getLocation(), boothDetail.getThumbnailUrl(),
        boothDetail.getImageUrls(), boothDetail.getCreatedAt());
  }
}
