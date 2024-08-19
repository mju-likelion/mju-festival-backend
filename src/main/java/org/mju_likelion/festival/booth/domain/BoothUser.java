package org.mju_likelion.festival.booth.domain;

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
@ToString(callSuper = true, of = {"booth", "user"})
@Entity(name = "booth_user")
public class BoothUser extends BaseEntity {

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "booth_id", nullable = false)
  private Booth booth;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  /**
   * 사용자와 부스가 자신의 사용자와 부스와 같은지 확인한다.
   *
   * @param user  사용자
   * @param booth 부스
   * @return 사용자와 부스가 자신의 사용자와 부스와 같은지 여부
   */
  public boolean isSameUserAndBooth(final User user, final Booth booth) {
    return this.user.equals(user) && this.booth.equals(booth);
  }
}
