package org.mju_likelion.festival.booth.domain;

import static org.mju_likelion.festival.common.exception.type.ErrorType.ALREADY_VISITED_BOOTH;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import org.mju_likelion.festival.common.exception.ConflictException;
import org.mju_likelion.festival.user.domain.User;

@Embeddable
public class BoothUsers {

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<BoothUser> boothUsers = new HashSet<>();

  /**
   * 사용자가 부스를 방문한다.
   *
   * @param user  사용자
   * @param booth 부스
   */
  public void visit(final User user, final Booth booth) {
    validateVisitBooth(user, booth);

    BoothUser boothUser = BoothUser.builder()
        .user(user)
        .booth(booth)
        .build();

    this.boothUsers.add(boothUser);
  }

  /**
   * 사용자가 이미 방문한 부스인지 검증한다.
   *
   * @param user  사용자
   * @param booth 부스
   */
  private void validateVisitBooth(final User user, final Booth booth) {
    if (isVisitedBooth(user, booth)) {
      throw new ConflictException(ALREADY_VISITED_BOOTH);
    }
  }

  /**
   * 사용자가 부스를 방문했는지 확인한다.
   *
   * @param user  사용자
   * @param booth 부스
   * @return 사용자가 부스를 방문했는지 여부
   */
  private boolean isVisitedBooth(final User user, final Booth booth) {
    return this.boothUsers.stream()
        .anyMatch(boothUser -> boothUser.isSameUserAndBooth(user, booth));
  }
}
