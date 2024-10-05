package org.mju_likelion.festival.term.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import lombok.ToString;
import org.mju_likelion.festival.user.domain.User;

@Embeddable
@ToString(callSuper = true, of = {"termUsers"})
public class TermUsers {

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<TermUser> termUsers = new HashSet<>();

  public void agree(final Term term, final User user) {
    TermUser termUser = new TermUser(user, term);
    this.termUsers.add(termUser);
  }
}
