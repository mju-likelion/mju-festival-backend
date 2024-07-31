package org.mju_likelion.festival.auth.util.token;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 인증 토큰을 나타내는 클래스.
 * <p>
 * private key와 만료 시간을 가지고 있다.
 * </p>
 * Redis 장애 시 인증 토큰을 저장하기 위해 사용된다.
 */
@Getter
@AllArgsConstructor
public class CredentialToken {

  private final String privateKey;
  private final LocalDateTime expirationTime;

  /**
   * 토큰 문자열을 CredentialToken 객체로 변환한다.
   *
   * @param tokenString 토큰 문자열
   * @return CredentialToken 객체
   */
  public static CredentialToken fromTokenString(String tokenString) {
    String privateKey = tokenString.split(",")[0];
    LocalDateTime expirationTime = LocalDateTime.parse(tokenString.split(",")[1]);
    return new CredentialToken(privateKey, expirationTime);
  }

  /**
   * CredentialToken 객체를 토큰 문자열로 변환한다.
   *
   * @return 토큰 문자열
   */
  public String toTokenString() {
    return privateKey + "," + expirationTime;
  }
}
