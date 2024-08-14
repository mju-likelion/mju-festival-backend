package org.mju_likelion.festival.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 응답 DTO.
 */
@Getter
@AllArgsConstructor
public class LoginResponse {

  private final String accessToken;

  @Override
  public String toString() {
    return "LoginResponse{" +
        "accessToken='" + accessToken + '\'' +
        '}';
  }
}
