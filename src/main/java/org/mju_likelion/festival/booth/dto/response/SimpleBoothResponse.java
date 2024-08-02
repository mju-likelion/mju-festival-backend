package org.mju_likelion.festival.booth.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.booth.domain.SimpleBooth;

@Getter
@AllArgsConstructor
public class SimpleBoothResponse {

  private final UUID id;
  private final String name;
  private final String description;
  private final String thumbnailUrl;

  public static SimpleBoothResponse from(final SimpleBooth simpleBooth) {
    return new SimpleBoothResponse(simpleBooth.getId(), simpleBooth.getName(),
        simpleBooth.getDescription(), simpleBooth.getThumbnailUrl());
  }
}
