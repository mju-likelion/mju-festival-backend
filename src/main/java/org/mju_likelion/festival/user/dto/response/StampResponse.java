package org.mju_likelion.festival.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.user.domain.UserStamp;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class StampResponse {

  private final int totalStampCount;
  private final int stampCount;

  public static StampResponse from(final UserStamp userStamp) {
    return new StampResponse(userStamp.getTotalStampCount(), userStamp.getStampCount());
  }

  @Override
  public String toString() {
    return "StampResponse{" +
        "totalStampCount=" + totalStampCount + '\'' +
        ", stampCount=" + stampCount + '\'' +
        '}';
  }
}
