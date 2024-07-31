package org.mju_likelion.festival.common.exception;

import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomException {

  public UnauthorizedException(ErrorType errorType) {
    super(errorType, HttpStatus.UNAUTHORIZED);
  }

  public UnauthorizedException(ErrorType errorType, String detail) {
    super(errorType, detail, HttpStatus.UNAUTHORIZED);
  }
}
