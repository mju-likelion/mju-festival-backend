package org.mju_likelion.festival.lost_item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 분실물 수정 요청 DTO.
 */
@Getter
@AllArgsConstructor
public class UpdateLostItemRequest {

  private String title;
  private String content;
  private String imageUrl;

  @Override
  public String toString() {
    return "UpdateLostItemRequest{" +
        "title='" + title + '\'' +
        ", content='" + content + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        '}';
  }
}
