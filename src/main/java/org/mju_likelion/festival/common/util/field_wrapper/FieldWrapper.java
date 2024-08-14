package org.mju_likelion.festival.common.util.field_wrapper;

import com.fasterxml.jackson.databind.JsonNode;
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

  /**
   * JSON 노드에서 문자열 필드를 가져와 필드 래퍼로 반환한다.
   *
   * @param node      JSON 노드
   * @param fieldName 필드 이름
   * @return 필드 래퍼
   */
  public static FieldWrapper<String> getStringFieldWrapper(
      final JsonNode node,
      final String fieldName) {

    if (node.has(fieldName)) {
      JsonNode fieldNode = node.get(fieldName);
      return new FieldWrapper<>(true, fieldNode.isNull() ? null : fieldNode.asText());
    } else {
      return new FieldWrapper<>(false, null);
    }
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
  public void doIfPresentAndNotNull(final Consumer<T> consumer) {
    if (isPresent() && value != null) {
      consumer.accept(value);
    }
  }

  /**
   * 값이 존재할 때만 consumer 를 실행한다.
   *
   * @param consumer 실행할 consumer
   */
  public void doIfPresent(final Consumer<T> consumer) {
    if (isPresent()) {
      consumer.accept(value);
    }
  }
}
