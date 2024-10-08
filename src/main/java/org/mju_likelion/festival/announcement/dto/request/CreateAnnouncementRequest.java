package org.mju_likelion.festival.announcement.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 공지사항 생성 요청 DTO
 */
@Getter
@AllArgsConstructor
public class CreateAnnouncementRequest {

  @NotBlank(message = "제목은 필수 입력값입니다.")
  private String title;

  @NotBlank(message = "내용은 필수 입력값입니다.")
  private String content;

  private String imageUrl;

  @Override
  public String toString() {
    return "CreateAnnouncementRequest{" +
        "title='" + title + '\'' +
        ", content='" + content + '\'' +
        ", imageUrl='" + imageUrl + '\'' +
        '}';
  }
}
