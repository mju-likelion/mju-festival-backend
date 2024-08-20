package org.mju_likelion.festival.term.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.user.domain.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, of = {"user", "term"})
@Entity(name = "term_user")
public class TermUser extends BaseEntity {

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "term_id", nullable = false)
  private Term term;

  public TermUser(final User user, final Term term) {
    this.user = user;
    this.term = term;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TermUser that)) {
      return false;
    }
    return Objects.equals(user, that.getUser()) && Objects.equals(term, that.getTerm());
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, term);
  }
}
