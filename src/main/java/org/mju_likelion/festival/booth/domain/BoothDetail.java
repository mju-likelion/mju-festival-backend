package org.mju_likelion.festival.booth.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

/**
 * 부스 상세 정보.
 */
@Getter
public class BoothDetail extends SimpleBooth {

  private final String location;
  private final String locationImageUrl;
  private final LocalDateTime createdAt;

  public BoothDetail(
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
}
