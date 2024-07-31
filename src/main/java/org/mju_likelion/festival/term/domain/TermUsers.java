package org.mju_likelion.festival.term.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import org.mju_likelion.festival.user.domain.User;

@Embeddable
public class TermUsers {

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<TermUser> termUsers = new HashSet<>();

  public void agree(Term term, User user) {
    TermUser termUser = TermUser.builder()
        .term(term)
        .user(user)
        .build();
    this.termUsers.add(termUser);
  }
}
