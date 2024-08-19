package org.mju_likelion.festival.term.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

  @Override
  public String toString() {
    return "TermUser{" +
        "user=" + user +
        ", term=" + term +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TermUser termUser)) {
      return false;
    }
    return user.equals(termUser.user) && term.equals(termUser.term);
  }

  @Override
  public int hashCode() {
    return user.hashCode() + term.hashCode();
  }
}
