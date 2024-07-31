package org.mju_likelion.festival.common.exception;

import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends CustomException {

  public ForbiddenException(ErrorType errorType) {
    super(errorType, HttpStatus.FORBIDDEN);
  }

  public ForbiddenException(ErrorType errorType, String detail) {
    super(errorType, detail, HttpStatus.FORBIDDEN);
  }
}
