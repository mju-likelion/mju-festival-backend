package org.mju_likelion.festival.booth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 부스 QR 생성 응답 DTO.
 */
@Getter
@AllArgsConstructor
public class BoothQrResponse {

  private final String qrCode;

  @Override
  public String toString() {
    return "BoothQrResponse{" +
        "qrCode='" + qrCode + '\'' +
        '}';
  }
}
