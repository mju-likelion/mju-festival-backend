package org.mju_likelion.festival.common.exception;

import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.http.HttpStatus;

public class ConflictException extends CustomException {

  public ConflictException(ErrorType errorType) {
    super(errorType, HttpStatus.CONFLICT);
  }

  public ConflictException(ErrorType errorType, String detail) {
    super(errorType, detail, HttpStatus.CONFLICT);
  }
}
