package org.mju_likelion.festival.auth.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.term.domain.Term;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TermResponse {

  private final UUID id;
  private final String title;
  private final String content;

  public static TermResponse of(Term term) {
    return new TermResponse(term.getId(), term.getTitle(), term.getContent());
  }
}
