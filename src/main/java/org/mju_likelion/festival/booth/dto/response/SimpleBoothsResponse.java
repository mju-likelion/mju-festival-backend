package org.mju_likelion.festival.booth.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.booth.domain.SimpleBooth;

/**
 * 부스 간단 정보 응답 DTO.
 */
@Getter
@AllArgsConstructor
public class SimpleBoothsResponse {

  private final List<SimpleBooth> simpleBooths;

  public static SimpleBoothsResponse from(final List<SimpleBooth> simpleBooths) {

    return new SimpleBoothsResponse(simpleBooths);
  }

  @Override
  public String toString() {
    return "SimpleBoothsResponse{" +
        "simpleBooths=" + simpleBooths +
        '}';
  }
}
