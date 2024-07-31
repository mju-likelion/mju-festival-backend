package org.mju_likelion.festival.auth.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginRequest {

  @NotNull(message = "학번이 누락되었습니다.")
  private String encryptedStudentId;

  @NotNull(message = "비밀번호가 누락되었습니다.")
  private String encryptedPassword;

  @NotNull(message = "키가 누락되었습니다.")
  private String key;

  @NotNull(message = "동의 항목이 누락되었습니다.")
  private Map<UUID, @AssertTrue(message = "동의 항목에 동의해야 합니다.") Boolean> terms;
}
