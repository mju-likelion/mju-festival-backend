package org.mju_likelion.festival.common.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorType {

  INVALID_REQUEST_BODY_ERROR(40000, "요청 바디가 잘못되었습니다."),
  MISSING_REQUEST_PARAMETER_ERROR(40001, "필수 요청 파라미터가 누락되었습니다."),
  INVALID_REQUEST_FORMAT_ERROR(40002, "요청 바디 형식이 잘못되었습니다."),
  MISSING_TERM_ERROR(40003, "동의 항목이 누락되었습니다."),
  FILE_EMPTY_ERROR(40004, "파일이 비어있습니다."),
  FILE_NOT_IMAGE_ERROR(40005, "이미지 파일이 아닙니다."),
  FILE_SIZE_EXCEED_ERROR(40006, "파일 크기가 초과되었습니다."),
  INVALID_RSA_KEY_STRATEGY_ERROR(40007, "유효하지 않은 RSA Key 전략입니다."),
  INVALID_BOOTH_QR_STRATEGY_ERROR(40008, "유효하지 않은 부스 QR 전략입니다."),
  INVALID_REQUEST_PARAMETER_ERROR(40009, "요청 파라미터가 잘못되었습니다."),

  INVALID_CREDENTIALS_ERROR(40100, "아이디나 비밀번호가 일치하지 않습니다."),
  NOT_AUTHENTICATED_ERROR(40101, "인증되지 않았습니다."),
  ALREADY_AUTHENTICATED_ERROR(40102, "이미 인증되었습니다."),
  JWT_NOT_FOUND_ERROR(40103, "JWT 토큰이 없습니다."),
  INVALID_JWT_ERROR(40104, "JWT 토큰이 유효하지 않습니다."),
  TOKEN_EXPIRED_ERROR(40105, "토큰이 만료되었습니다."),

  USER_ONLY_ERROR(40300, "사용자만 접근할 수 있습니다."),
  ADMIN_ONLY_ERROR(40301, "관리자만 접근할 수 있습니다."),
  STUDENT_COUNCIL_ONLY_ERROR(40302, "학생회만 접근할 수 있습니다."),
  BOOTH_MANAGER_ONLY_ERROR(40303, "부스 관리자만 접근할 수 있습니다."),
  NOT_BOOTH_OWNER_ERROR(40304, "해당 부스 관리자만 접근할 수 있습니다."),

  NO_RESOURCE_ERROR(40400, "해당 리소스를 찾을 수 없습니다."),
  BOOTH_NOT_FOUND_ERROR(40401, "해당 부스를 찾을 수 없습니다."),
  CREDENTIAL_KEY_NOT_FOUND_ERROR(40402, "자격 증명 키를 찾을 수 없습니다."),
  BOOTH_QR_NOT_FOUND_ERROR(40403, "해당 부스 QR을 찾을 수 없습니다."),
  ADMIN_NOT_FOUND_ERROR(40404, "해당 관리자를 찾을 수 없습니다."),
  USER_NOT_FOUND_ERROR(40405, "해당 사용자를 찾을 수 없습니다."),
  PAGE_OUT_OF_BOUND_ERROR(40406, "페이지 범위를 벗어났습니다."),
  ANNOUNCEMENT_NOT_FOUND_ERROR(40407, "해당 공지사항을 찾을 수 없습니다."),

  METHOD_NOT_ALLOWED_ERROR(40500, "허용되지 않은 HTTP 메소드입니다."),

  HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_ERROR(40600, "수락할 수 없는 미디어 타입입니다."),

  ALREADY_VISITED_BOOTH(40900, "이미 방문한 부스입니다."),

  HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR(41500, "지원하지 않는 미디어 타입입니다."),

  API_ERROR(50000, "MSI API 호출 중 오류가 발생했습니다."),
  UUID_FORMAT_ERROR(50001, "UUID 형식이 잘못되었습니다."),
  IMAGE_UPLOAD_ERROR(50002, "이미지 업로드 중 오류가 발생했습니다."),
  TOKEN_GENERATE_ERROR(50003, "토큰 생성 중 오류가 발생했습니다."),
  TOKEN_PARSE_ERROR(50004, "토큰 파싱 중 오류가 발생했습니다."),
  ENCRYPT_ERROR(50005, "암호화에 실패했습니다."),
  DECRYPT_ERROR(50006, "복호화에 실패했습니다."),
  RSA_KEY_ERROR(50007, "RSA 키 처리 중 오류가 발생했습니다."),


  REDIS_ERROR(60000, "Redis 에러가 발생했습니다."),

  UNEXPECTED_ERROR(99999, "예기치 않은 오류가 발생했습니다.");

  private final int code;
  private final String message;
}
