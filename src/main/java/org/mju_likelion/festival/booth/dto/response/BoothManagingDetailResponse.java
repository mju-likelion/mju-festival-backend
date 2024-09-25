package org.mju_likelion.festival.booth.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 부스 소유권 응답 DTO.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoothManagingDetailResponse {

  private final Boolean isOwner;
  private final Boolean isEventBooth;

  public static BoothManagingDetailResponse of(final boolean isOwner, final boolean isEventBooth) {
    return new BoothManagingDetailResponse(isOwner, isEventBooth);
  }

  @Override
  public String toString() {
    return "BoothOwnershipResponse{" +
        "isOwner=" + isOwner +
        ", isEventBooth=" + isEventBooth +
        '}';
  }
}
