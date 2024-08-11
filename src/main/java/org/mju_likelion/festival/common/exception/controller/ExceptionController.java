package org.mju_likelion.festival.common.exception.controller;

import static org.mju_likelion.festival.common.exception.type.ErrorType.HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.common.exception.CustomException;
import org.mju_likelion.festival.common.exception.dto.ErrorResponse;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> customExceptionHandler(final CustomException e) {
    writeLog(e);
    return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.res(e));
  }

  // MethodArgumentNotValidException 예외를 처리하는 핸들러(요청 바디가 잘못된 경우)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> springValidationExceptionHandler(
      final MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

    BadRequestException badRequestException = new BadRequestException(
        ErrorType.INVALID_REQUEST_BODY_ERROR, message);

    writeLog(badRequestException);
    return ResponseEntity.badRequest().body(ErrorResponse.res(badRequestException));
  }

  // MethodArgumentTypeMismatchException 예외를 처리하는 핸들러 (요청 파라메터의 타입이 잘못된 경우)
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> methodArgumentTypeMismatchExceptionHandler(
      final MethodArgumentTypeMismatchException e) {
    BadRequestException badRequestException = new BadRequestException(
        ErrorType.INVALID_REQUEST_PARAMETER_ERROR, e.getName());

    writeLog(badRequestException);
    return ResponseEntity.badRequest().body(ErrorResponse.res(badRequestException));
  }

  // MissingServletRequestParameterException 예외를 처리하는 핸들러(필수 요청 파라미터가 누락된 경우)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> missingServletRequestParameterExceptionHandler(
      final MissingServletRequestParameterException e) {
    BadRequestException badRequestException = new BadRequestException(
        ErrorType.MISSING_REQUEST_PARAMETER_ERROR, e.getParameterName());

    writeLog(badRequestException);
    return ResponseEntity.badRequest().body(ErrorResponse.res(badRequestException));
  }

  // HttpMessageNotReadableException 예외를 처리하는 핸들러(Body가 잘못된 경우(json 형식이 잘못된 경우))
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException httpMessageNotReadableException) {
    BadRequestException badRequestException = new BadRequestException(
        ErrorType.INVALID_REQUEST_FORMAT_ERROR, httpMessageNotReadableException.getMessage());

    writeLog(badRequestException);
    return ResponseEntity.badRequest().body(ErrorResponse.res(badRequestException));
  }

  // HttpRequestMethodNotSupportedException 예외를 처리하는 핸들러(요청의 메소드가 잘못된 경우)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
    BadRequestException badRequestException = new BadRequestException(
        ErrorType.METHOD_NOT_ALLOWED_ERROR, httpRequestMethodNotSupportedException.getMessage());

    writeLog(badRequestException);
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .body(ErrorResponse.res(badRequestException));
  }

  // NoResourceFoundException 예외를 처리하는 핸들러(리소스를 찾을 수 없는 경우(URI가 잘못된 경우))
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
      NoResourceFoundException noResourceFoundException) {
    BadRequestException badRequestException = new BadRequestException(
        ErrorType.NO_RESOURCE_ERROR, noResourceFoundException.getMessage());

    writeLog(badRequestException);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.res(badRequestException));
  }

  // HttpMediaTypeNotAcceptableException 예외를 처리하는 핸들러(요청의 Accept 헤더가 잘못된 경우)
  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotAcceptableException(
      HttpMediaTypeNotAcceptableException httpMediaTypeNotAcceptableException) {
    BadRequestException badRequestException = new BadRequestException(
        ErrorType.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE_ERROR,
        httpMediaTypeNotAcceptableException.getMessage());

    writeLog(badRequestException);

    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        .body(ErrorResponse.res(badRequestException));
  }

  // HttpMediaTypeNotSupportedException 예외를 처리하는 핸들러(요청의 Content-Type 헤더가 잘못된 경우)
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException) {
    BadRequestException badRequestException = new BadRequestException(
        HTTP_MEDIA_TYPE_NOT_SUPPORTED_ERROR, httpMediaTypeNotSupportedException.getMessage());

    writeLog(badRequestException);
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        .body(ErrorResponse.res(badRequestException));
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
