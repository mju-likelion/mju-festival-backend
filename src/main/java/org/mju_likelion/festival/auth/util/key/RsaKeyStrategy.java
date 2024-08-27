package org.mju_likelion.festival.auth.util.key;

import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_RSA_KEY_STRATEGY_ERROR;

import org.mju_likelion.festival.common.exception.BadRequestException;

/**
 * RSA Key 전략 Enum
 * <p>
 * RSA Key 를 관리하는 전략을 정의한다.
 */
public enum RsaKeyStrategy {
  REDIS, // Private Key 를 Redis 에 저장
  TOKEN; // Private Key 와 만료 시간을 Token 에 저장

  public static RsaKeyStrategy fromString(final String value) {
    try {
      return valueOf(value.toUpperCase());
    } catch (Exception e) {
      throw new BadRequestException(INVALID_RSA_KEY_STRATEGY_ERROR, e.getMessage());
    }
  }
}
