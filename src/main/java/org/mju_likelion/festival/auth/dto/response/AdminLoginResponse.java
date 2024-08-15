package org.mju_likelion.festival.auth.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.admin.domain.AdminRole;

/**
 * 관리자 로그인 응답 DTO.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminLoginResponse {

  private final String accessToken;
  private final AdminRole role;

  public static AdminLoginResponse of(final String accessToken, final AdminRole role) {
    return new AdminLoginResponse(accessToken, role);
  }

  @Override
  public String toString() {
    return "LoginResponse{" +
        "accessToken='" + accessToken + '\'' +
        '}';
  }
}
