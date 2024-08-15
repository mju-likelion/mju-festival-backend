package org.mju_likelion.festival.auth.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 유저 로그인 요청 DTO.
 */
@Getter
@AllArgsConstructor
public class UserLoginRequest {

  @NotBlank(message = "학번이 누락되었습니다.")
  private String encryptedStudentId;

  @NotBlank(message = "비밀번호가 누락되었습니다.")
  private String encryptedPassword;

  @NotBlank(message = "키가 누락되었습니다.")
  private String key;

  @NotNull(message = "동의 항목이 누락되었습니다.")
  @Size(min = 1, message = "적어도 하나의 동의 항목이 필요합니다.")
  private Map<UUID, @AssertTrue(message = "동의 항목에 동의해야 합니다.") Boolean> terms;

  @Override
  public String toString() {
    return "UserLoginRequest{" +
        "encryptedStudentId='" + encryptedStudentId + '\'' +
        ", encryptedPassword='" + encryptedPassword + '\'' +
        ", key='" + key + '\'' +
        ", terms=" + terms +
        '}';
  }
}
