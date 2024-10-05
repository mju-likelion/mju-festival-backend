package org.mju_likelion.festival.auth.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 유저 로그인 응답 DTO.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginResponse {

  private final String accessToken;

  public static UserLoginResponse of(final String accessToken) {
    return new UserLoginResponse(accessToken);
  }

  @Override
  public String toString() {
    return "LoginResponse{" +
        "accessToken='" + accessToken + '\'' +
        '}';
  }
}
