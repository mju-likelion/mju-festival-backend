package org.mju_likelion.festival.term.domain;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 간단한 약관 정보.
 */
@Getter
@AllArgsConstructor
public class SimpleTerm {

  private final UUID id;
  private final String title;
  private final String content;

  @Override
  public String toString() {
    return "SimpleTerm{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", content='" + content + '\'' +
        '}';
  }
}
