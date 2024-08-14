package org.mju_likelion.festival.booth.domain;

import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_BOOTH_QR_STRATEGY_ERROR;

import org.mju_likelion.festival.common.exception.BadRequestException;

/**
 * 부스 QR 전략을 나타내는 Enum.
 */
public enum BoothQrStrategy {
  REDIS,
  TOKEN;

  public static BoothQrStrategy fromString(final String value) {
    try {
      return valueOf(value.toUpperCase());
    } catch (Exception e) {
      throw new BadRequestException(INVALID_BOOTH_QR_STRATEGY_ERROR, e.getMessage());
    }
  }
}
