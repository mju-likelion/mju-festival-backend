package org.mju_likelion.festival.common.exception.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.common.exception.CustomException;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

  private final int errorCode;
  private final String message;
  private final String detail;

  public static ErrorResponse res(final CustomException customException) {
    int errorCode = customException.getErrorType().getCode();
    String message = customException.getErrorType().getMessage();
    String detail = customException.getDetail();
    return new ErrorResponse(errorCode, message, detail);
  }

  public static ErrorResponse res(final Exception e) {
    return new ErrorResponse(9999, e.getMessage(), null);
  }

  @Override
  public String toString() {
    return "ErrorResponse{" +
        "errorCode=" + errorCode +
        ", message='" + message +
        ", detail='" + detail +
        '}';
  }
}
