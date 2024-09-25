package org.mju_likelion.festival.booth.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.booth.domain.SimpleBooth;

/**
 * 부스 간단 정보 응답 DTO.
 */
@Getter
@AllArgsConstructor
public class SimpleBoothsResponse {

  private final UUID id;
  private final String name;
  private final String imageUrl;

  public static SimpleBoothsResponse from(final SimpleBooth simpleBooths) {

    return new SimpleBoothsResponse(simpleBooths.getId(), simpleBooths.getName(),
        simpleBooths.getImageUrl());
  }

  @Override
  public String toString() {
    return "SimpleBoothsResponse{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        '}';
  }
}
