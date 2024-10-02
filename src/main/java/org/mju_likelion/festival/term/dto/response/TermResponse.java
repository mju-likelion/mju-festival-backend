package org.mju_likelion.festival.term.dto.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.term.domain.SimpleTerm;

/**
 * 약관 조회 응답 DTO.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class TermResponse {

  private UUID id;
  private String title;
  private String content;

  public static TermResponse of(final SimpleTerm simpleTerm) {
    return new TermResponse(simpleTerm.getId(), simpleTerm.getTitle(), simpleTerm.getContent());
  }

  @Override
  public String toString() {
    return "TermResponse{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        '}';
  }
}
