package org.mju_likelion.festival.common.util.field_wrapper;

import java.util.function.Consumer;

/**
 * 필드 래퍼
 * <p>
 * JSON 에서 필드가 존재하는지 여부와 값의 유무를 구분하기 위한 래퍼 클래스
 */
public class FieldWrapper<T> {

  private final boolean present; // 필드가 존재하는지 여부
  private final T value; // 필드 값

  public FieldWrapper(boolean present, T value) {
    this.present = present;
    this.value = value;
  }

  public boolean isPresent() {
    return present;
  }

  public T getValue() {
    return value;
  }

  /**
   * 값이 존재하고 null 이 아닐 때만 consumer 를 실행한다.
   *
   * @param consumer 실행할 consumer
   */
  public void doIfPresentAndNotNull(Consumer<T> consumer) {
    if (isPresent() && value != null) {
      consumer.accept(value);
    }
  }

  /**
   * 값이 존재할 때만 consumer 를 실행한다.
   *
   * @param consumer 실행할 consumer
   */
  public void doIfPresent(Consumer<T> consumer) {
    if (isPresent()) {
      consumer.accept(value);
    }
  }
}
