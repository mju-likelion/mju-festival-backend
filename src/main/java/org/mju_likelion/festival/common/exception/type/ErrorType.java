package org.mju_likelion.festival.common.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

  ENCRYPT_ERROR(1000, "암호화에 실패했습니다."),
  DECRYPT_ERROR(1001, "복호화에 실패했습니다."),
  RSA_KEY_ERROR(1002, "RSA 키 처리 중 오류가 발생했습니다."),
  CREDENTIAL_TOKEN_EXPIRED(1003, "자격 증명 토큰이 만료되었습니다."),

  INVALID_REQUEST_BODY(4000, "요청 바디가 잘못되었습니다."),

  REDIS_ERROR(6000, "Redis 에러가 발생했습니다.");


  private final int code;
  private final String message;
}
