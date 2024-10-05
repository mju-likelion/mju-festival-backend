package org.mju_likelion.festival.booth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 부스 수정 요청 DTO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBoothRequest {

  private String description;

  @Override
  public String toString() {
    return "UpdateBoothRequest{" +
        ", description='" + description + '\'' +
        '}';
  }
}
