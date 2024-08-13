package org.mju_likelion.festival.lost_item.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 분실물 등록 요청 DTO
 */
@Getter
@AllArgsConstructor
public class CreateLostItemRequest {

  @NotBlank(message = "제목은 필수 입력값입니다.")
  private String title;

  @NotBlank(message = "내용은 필수 입력값입니다.")
  private String content;

  @NotBlank(message = "분실물 이미지는 필수 입력값입니다.")
  private String imageUrl;
}
