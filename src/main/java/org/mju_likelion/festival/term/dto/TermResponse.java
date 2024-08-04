package org.mju_likelion.festival.term.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.term.domain.SimpleTerm;

/**
 * 약관 조회 응답 DTO.
 */
@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TermResponse {

  private final UUID id;
  private final String title;
  private final String content;

  public static TermResponse of(SimpleTerm simpleTerm) {
    return new TermResponse(simpleTerm.getId(), simpleTerm.getTitle(), simpleTerm.getContent());
  }
}
