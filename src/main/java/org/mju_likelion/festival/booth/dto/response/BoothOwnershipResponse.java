package org.mju_likelion.festival.booth.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 부스 소유권 응답 DTO.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoothOwnershipResponse {

  private final Boolean isOwner;

  public static BoothOwnershipResponse from(final boolean isOwner) {
    return new BoothOwnershipResponse(isOwner);
  }

  @Override
  public String toString() {
    return "BoothOwnershipResponse{" +
        "isOwner=" + isOwner +
        '}';
  }
}
