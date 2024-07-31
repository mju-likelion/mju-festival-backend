package org.mju_likelion.festival.auth.domain;

/**
 * RSA Key 전략 Enum
 * <p>
 * RSA Key 를 관리하는 전략을 정의한다.
 */
public enum RsaKeyStrategy {
  REDIS, // Private Key 를 Redis 에 저장
  TOKEN // Private Key 와 만료 시간을 Token 에 저장
}
