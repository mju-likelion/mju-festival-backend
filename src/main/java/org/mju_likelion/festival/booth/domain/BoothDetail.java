package org.mju_likelion.festival.booth.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BoothDetail extends SimpleBooth {

  private final String location;
  private final List<String> imageUrls;
  private final LocalDateTime createdAt;

  public BoothDetail(
      final UUID id,
      final String name,
      final String description,
      final String location,
      final String thumbnailUrl,
      final List<String> imageUrls,
      final LocalDateTime createdAt) {
    
    super(id, name, description, thumbnailUrl);
    this.location = location;
    this.imageUrls = imageUrls;
    this.createdAt = createdAt;
  }
}
