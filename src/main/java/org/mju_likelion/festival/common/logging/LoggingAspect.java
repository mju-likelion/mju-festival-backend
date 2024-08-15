package org.mju_likelion.festival.common.logging;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

  private final HttpServletRequest request;

  public LoggingAspect(HttpServletRequest request) {
    this.request = request;
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
  private void getMapping() {
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
  private void postMapping() {
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
  private void putMapping() {
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
  private void deleteMapping() {
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
  private void patchMapping() {
  }

  @Pointcut("getMapping() || postMapping() || putMapping() || deleteMapping() || patchMapping()")
  private void allMapping() {
  }

  @Pointcut("execution(* org.mju_likelion.festival.*.controller.*Controller.*(..))")
  private void controllerPointCut() {
  }

  @Pointcut("@annotation(org.springframework.web.bind.annotation.ExceptionHandler)")
  private void exceptionHandlerCut() {
  }

  @Before("allMapping()")
  public void requestLog(final JoinPoint joinPoint) {
    String requestId = (String) request.getAttribute("requestId");
    String userId = (String) request.getAttribute("userId");
    String methodArguments = Arrays.stream(joinPoint.getArgs())
        .map(arg -> arg == null ? "null" : arg.toString())
        .collect(Collectors.joining(", "));

    log.info(
        ">> REQUEST >> [ID: {}, USERID: {}] Controller: {} || Method: {}() || Arguments: [{}]",
        requestId,
        userId,
        joinPoint.getTarget().getClass().getSimpleName(),
        joinPoint.getSignature().getName(),
        methodArguments
    );
  }

  @AfterReturning(value = "controllerPointCut() || exceptionHandlerCut()", returning = "response")
  public void responseLog(final JoinPoint joinPoint, final ResponseEntity<?> response) {
    String requestId = (String) request.getAttribute("requestId");
    String userId = (String) request.getAttribute("userId");

    log.info(
        "<< RESPONSE << [ID: {}, USERID: {}] Controller: {} || Method: {}() || ResponseBody: {}",
        requestId,
        userId,
        joinPoint.getTarget().getClass().getSimpleName(),
        joinPoint.getSignature().getName(),
        response.getBody()
    );
  }
}
