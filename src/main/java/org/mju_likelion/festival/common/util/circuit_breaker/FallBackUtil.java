package org.mju_likelion.festival.common.util.circuit_breaker;

import lombok.extern.slf4j.Slf4j;
import org.mju_likelion.festival.common.exception.CustomException;

@Slf4j
public class FallBackUtil {

  public static void handleFallBack(final Exception e) {
    if (e instanceof CustomException) {
      throw (CustomException) e;
    }

    StackTraceElement[] stackTrace = new Throwable().getStackTrace();
    StackTraceElement caller = stackTrace[1];
    writeLog(e, caller);
  }

  private static void writeLog(final Exception e, final StackTraceElement caller) {
    log.info("Fallback to method {}(): {}, {}",
        caller.getMethodName(),
        e.getClass().getSimpleName(),
        e.getMessage()
    );
  }
}
