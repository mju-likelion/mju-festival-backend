package org.mju_likelion.festival.common.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.common.exception.CustomException;
import org.mju_likelion.festival.common.exception.dto.ErrorResponse;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> customExceptionHandler(final CustomException e) {
    writeLog(e);
    return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.res(e));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> springValidationExceptionHandler(
      final MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

    BadRequestException badRequestException = new BadRequestException(
        ErrorType.INVALID_REQUEST_BODY, message);

    writeLog(badRequestException);
    return ResponseEntity.badRequest().body(ErrorResponse.res(badRequestException));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> exceptionHandler(final Exception e) {
    writeLog(e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.res(e));
  }

  private void writeLog(CustomException customException) {
    ErrorType errorType = customException.getErrorType();

    String exceptionName = customException.getClass().getSimpleName();
    String message = errorType.getMessage();
    String detail = customException.getDetail();

    log.error("[{}]{}:{}", exceptionName, message, detail);
  }

  private void writeLog(Exception exception) {
    String exceptionName = exception.getClass().getSimpleName();
    String message = exception.getMessage();
    log.error("[{}]:{}", exceptionName, message);
  }
}
