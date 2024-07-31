package org.mju_likelion.festival.common.exception;

import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.http.HttpStatus;

public class BadRequestException extends CustomException {

  public BadRequestException(ErrorType errorType) {
    super(errorType, HttpStatus.BAD_REQUEST);
  }

  public BadRequestException(ErrorType errorType, String detail) {
    super(errorType, detail, HttpStatus.BAD_REQUEST);
  }
}
