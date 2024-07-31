package org.mju_likelion.festival.common.exception;

import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {

  public NotFoundException(ErrorType errorType) {
    super(errorType, HttpStatus.NOT_FOUND);
  }

  public NotFoundException(ErrorType errorType, String detail) {
    super(errorType, detail, HttpStatus.NOT_FOUND);
  }
}
