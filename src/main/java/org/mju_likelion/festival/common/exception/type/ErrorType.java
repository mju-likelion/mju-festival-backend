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
  CREDENTIAL_KEY_INVALID(1004, "자격 증명 키가 유효하지 않습니다."),

  INVALID_REQUEST_BODY(4000, "요청 바디가 잘못되었습니다."),

  INVALID_CREDENTIALS(4010, "아이디나 비밀번호가 일치하지 않습니다."),

  MISSING_TERM(4040, "동의 항목이 누락되었습니다."),

  INVALID_JWT(5000, "JWT 토큰이 유효하지 않습니다."),
  API_ERROR(5001, "API 호출 중 오류가 발생했습니다."),

  REDIS_ERROR(6000, "Redis 에러가 발생했습니다.");


  private final int code;
  private final String message;
}
