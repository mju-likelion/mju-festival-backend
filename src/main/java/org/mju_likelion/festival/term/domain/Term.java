package org.mju_likelion.festival.term.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mju_likelion.festival.common.domain.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, of = {"title", "content", "sequence"})
@Entity(name = "term")
public class Term extends BaseEntity {

  @Transient
  private final int TITLE_LENGTH = 100;
  @Transient
  private final int CONTENT_LENGTH = 4000;

  @Column(nullable = false, length = TITLE_LENGTH)
  private String title;

  @Column(nullable = false, length = CONTENT_LENGTH)
  private String content;

  @Column(unique = true, nullable = false)
  private Short sequence;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Term that)) {
      return false;
    }
    return Objects.equals(sequence, that.sequence);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sequence);
  }
}
