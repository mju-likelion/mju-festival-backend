package org.mju_likelion.festival.booth.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 부스 소유권 응답 DTO.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BoothManagingDetailResponse {

  private Boolean isOwner;

  public static BoothManagingDetailResponse of(final boolean isOwner) {
    return new BoothManagingDetailResponse(isOwner);
  }

  @Override
  public String toString() {
    return "BoothOwnershipResponse{" +
        "isOwner=" + isOwner +
        '}';
  }
}
