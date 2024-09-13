package org.mju_likelion.festival.booth.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.booth.domain.BoothDetail;

/**
 * 부스 상세 응답 DTO.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoothDetailResponse {

  private final UUID id;
  private final String name;
  private final String description;
  private final String department;
  private final String location;
  private final String imageUrl;
  private final String locationImageUrl;
  private final LocalDateTime createdAt;

  public static BoothDetailResponse from(final BoothDetail boothDetail) {
    return new BoothDetailResponse(boothDetail.getId(), boothDetail.getName(),
        boothDetail.getDescription(), boothDetail.getDepartment(), boothDetail.getLocation(),
        boothDetail.getImageUrl(), boothDetail.getLocationImageUrl(), boothDetail.getCreatedAt());
  }

  @Override
  public String toString() {
    return "BoothDetailResponse{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", department='" + department + '\'' +
        ", location='" + location + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        ", locationImageUrl='" + locationImageUrl + '\'' +
        ", createdAt=" + createdAt +
        '}';
  }
}
