package org.mju_likelion.festival.common.util.null_handler;

import java.util.function.Consumer;

/**
 * Null 처리 유틸리티
 */
public class NullHandler {

  /**
   * 값이 null 이 아닐 때만 void 함수를 실행한다.
   *
   * @param value          값
   * @param updateFunction 실행할 함수
   */
  public static <T> void doIfNotNull(final T value, final Consumer<T> updateFunction) {
    if (value != null) {
      updateFunction.accept(value);
    }
  }
}
