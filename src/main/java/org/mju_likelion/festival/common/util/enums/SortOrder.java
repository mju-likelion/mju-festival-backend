package org.mju_likelion.festival.common.util.enums;

import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_SORT_ORDER_ERROR;

import lombok.Getter;
import org.mju_likelion.festival.common.exception.BadRequestException;

/**
 * 정렬 순서를 나타내는 Enum
 */
@Getter
public enum SortOrder {

  ASC,
  DESC;

  public static SortOrder fromString(final String value) {
    try {
      return SortOrder.valueOf(value.toUpperCase());
    } catch (Exception e) {
      throw new BadRequestException(INVALID_SORT_ORDER_ERROR);
    }
  }
}
