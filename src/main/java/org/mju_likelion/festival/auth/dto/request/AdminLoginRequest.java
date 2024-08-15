package org.mju_likelion.festival.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 관리자 로그인 요청 DTO.
 */
@Getter
@AllArgsConstructor
public class AdminLoginRequest {

  @NotBlank(message = "관리자 아이디가 누락되었습니다.")
  private String encryptedLoginId;

  @NotBlank(message = "비밀번호가 누락되었습니다.")
  private String encryptedPassword;

  @NotBlank(message = "키가 누락되었습니다.")
  private String key;

  @Override
  public String toString() {
    return "AdminLoginRequest{" +
        "encryptedLoginId='" + encryptedLoginId + '\'' +
        ", encryptedPassword='" + encryptedPassword + '\'' +
        ", key='" + key + '\'' +
        '}';
  }
}
