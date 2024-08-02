package org.mju_likelion.festival.term.domain;

import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.TERM_CONTENT_LENGTH;
import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.TERM_TITLE_LENGTH;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.Objects;
import lombok.Getter;
import org.mju_likelion.festival.common.domain.BaseEntity;

@Getter
@Entity(name = "term")
public class Term extends BaseEntity {

  @Column(nullable = false, length = TERM_TITLE_LENGTH)
  private String title;

  @Column(nullable = false, length = TERM_CONTENT_LENGTH)
  private String content;

  @Column(nullable = false)
  private Short order;

  @Override
  public String toString() {
    return "Term{" +
        "title='" + title + '\'' +
        ", content='" + content + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Term term)) {
      return false;
    }
    return this.id.equals(term.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
