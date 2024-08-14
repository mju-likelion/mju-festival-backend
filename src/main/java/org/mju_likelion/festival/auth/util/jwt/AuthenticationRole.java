package org.mju_likelion.festival.auth.util.jwt;

import org.mju_likelion.festival.admin.domain.AdminRole;

/**
 * 인증에 사용되는 역할을 나타내는 Enum.
 */
public enum AuthenticationRole {
  STUDENT_COUNCIL, BOOTH_MANAGER, USER;

  /**
   * AdminRole 로부터 AuthenticationRole 을 생성한다.
   *
   * @param adminRole AdminRole
   * @return AuthenticationRole
   */
  public static AuthenticationRole from(final AdminRole adminRole) {
    return switch (adminRole) {
      case STUDENT_COUNCIL -> STUDENT_COUNCIL;
      case BOOTH_MANAGER -> BOOTH_MANAGER;
    };
  }

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
