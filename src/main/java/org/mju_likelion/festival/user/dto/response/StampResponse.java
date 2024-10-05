package org.mju_likelion.festival.user.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.booth.domain.BoothNames;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class StampResponse {

  private final List<String> participatedBoothNames;
  private final int boothsCountToComplete;

  public static StampResponse of(final BoothNames boothNames, final int boothsCountToComplete) {
    return new StampResponse(boothNames.getBoothNames(), boothsCountToComplete);
  }

  @Override
  public String toString() {
    return "StampResponse{" +
        "participatedBoothNames=" + participatedBoothNames +
        ", boothsCountToComplete=" + boothsCountToComplete +
        '}';
  }
}
