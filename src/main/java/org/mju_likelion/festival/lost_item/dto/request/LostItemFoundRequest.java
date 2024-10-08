package org.mju_likelion.festival.lost_item.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 분실물 찾음 처리 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LostItemFoundRequest {

  @NotBlank(message = "수령인 정보는 필수값입니다.")
  private String retrieverInfo;

  @Override
  public String toString() {
    return "LostItemFoundRequest{" +
        "retrieverInfo='" + retrieverInfo + '\'' +
        '}';
  }
}
