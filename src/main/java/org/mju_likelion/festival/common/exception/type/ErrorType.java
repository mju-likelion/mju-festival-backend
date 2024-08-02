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
  MISSING_REQUEST_PARAMETER(4001, "필수 요청 파라미터가 누락되었습니다."),
  INVALID_REQUEST_FORMAT_ERROR(4002, "요청 바디 형식이 잘못되었습니다."),

  INVALID_CREDENTIALS(4010, "아이디나 비밀번호가 일치하지 않습니다."),

  MISSING_TERM(4040, "동의 항목이 누락되었습니다."),
  NO_RESOURCE_ERROR(4041, "해당 리소스를 찾을 수 없습니다."),

  METHOD_NOT_ALLOWED_ERROR(4050, "허용되지 않은 HTTP 메소드입니다."),

  HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_ERROR(4060, "수락할 수 없는 미디어 타입입니다."),

  HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR(4150, "지원하지 않는 미디어 타입입니다."),

  INVALID_JWT(5000, "JWT 토큰이 유효하지 않습니다."),
  API_ERROR(5001, "API 호출 중 오류가 발생했습니다."),

  REDIS_ERROR(6000, "Redis 에러가 발생했습니다.");


  private final int code;
  private final String message;
}
