package org.mju_likelion.festival.common.exception;

import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends CustomException {

  public ForbiddenException(final ErrorType errorType) {
    super(errorType, HttpStatus.FORBIDDEN);
  }

  public ForbiddenException(final ErrorType errorType, final String detail) {
    super(errorType, detail, HttpStatus.FORBIDDEN);
  }
}
