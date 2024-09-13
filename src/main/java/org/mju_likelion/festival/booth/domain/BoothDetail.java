package org.mju_likelion.festival.booth.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

/**
 * 부스 상세 정보.
 */
@Getter
public class BoothDetail extends SimpleBooth {

  private final String department;
  private final String location;
  private final String locationImageUrl;
  private final LocalDateTime createdAt;

  public BoothDetail(
      final UUID id,
      final String name,
      final String description,
      final String department,
      final String location,
      final String imageUrl,
      final String locationImageUrl,
      final LocalDateTime createdAt) {

    super(id, name, description, imageUrl);
    this.department = department;
    this.location = location;
    this.locationImageUrl = locationImageUrl;
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "BoothDetail{" +
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
