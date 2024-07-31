package org.mju_likelion.festival.common.exception;

import lombok.Getter;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.http.HttpStatus;

@Getter
public abstract class CustomException extends RuntimeException {

  private final ErrorType errorType;
  private final String detail;
  private final HttpStatus httpStatus;

  protected CustomException(final ErrorType errorType, HttpStatus httpStatus) {
    super(errorType.getMessage());
    this.errorType = errorType;
    this.detail = null;
    this.httpStatus = httpStatus;
  }

  protected CustomException(final ErrorType errorType, final String detail, HttpStatus httpStatus) {
    super(errorType.getMessage());
    this.errorType = errorType;
    this.detail = detail;
    this.httpStatus = httpStatus;
  }
}
