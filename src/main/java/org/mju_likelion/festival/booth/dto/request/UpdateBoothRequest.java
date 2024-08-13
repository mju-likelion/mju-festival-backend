package org.mju_likelion.festival.booth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 부스 수정 요청 DTO
 */
@Getter
@AllArgsConstructor
public class UpdateBoothRequest {

  private String name;
  private String description;
  private String location;
  private String locationImageUrl;
  private String imageUrl;
}
