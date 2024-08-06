package org.mju_likelion.festival.booth.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.mju_likelion.festival.booth.domain.BoothDetail;

/**
 * 부스 상세 응답 DTO.
 */
@Getter
public class BoothDetailResponse extends SimpleBoothResponse {

  private final String location;
  private final String locationImageUrl;
  private final LocalDateTime createdAt;

  public BoothDetailResponse(
      final UUID id,
      final String name,
      final String description,
      final String location,
      final String imageUrl,
      final String locationImageUrl,
      final LocalDateTime createdAt) {

    super(id, name, description, imageUrl);
    this.location = location;
    this.locationImageUrl = locationImageUrl;
    this.createdAt = createdAt;
  }

  public static BoothDetailResponse from(final BoothDetail boothDetail) {
    return new BoothDetailResponse(boothDetail.getId(), boothDetail.getName(),
        boothDetail.getDescription(), boothDetail.getLocation(), boothDetail.getImageUrl(),
        boothDetail.getLocationImageUrl(), boothDetail.getCreatedAt());
  }
}
