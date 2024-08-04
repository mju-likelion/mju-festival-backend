package org.mju_likelion.festival.common.exception.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.common.exception.CustomException;
import org.mju_likelion.festival.common.exception.type.ErrorType;

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
    ErrorType errorType = ErrorType.UNEXPECTED_ERROR;
    int errorCode = errorType.getCode();
    String message = errorType.getMessage();
    String detail = e.getMessage();
    return new ErrorResponse(errorCode, message, detail);
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
