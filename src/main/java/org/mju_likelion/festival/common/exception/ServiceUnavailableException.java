package org.mju_likelion.festival.common.exception;

import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends CustomException {

  public ServiceUnavailableException(final ErrorType errorType) {
    super(errorType, HttpStatus.SERVICE_UNAVAILABLE);
  }

  public ServiceUnavailableException(final ErrorType errorType, final String detail) {
    super(errorType, detail, HttpStatus.SERVICE_UNAVAILABLE);
  }
}
