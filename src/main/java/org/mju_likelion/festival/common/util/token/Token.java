package org.mju_likelion.festival.common.util.token;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토큰을 나타내는 클래스.
 */
@Getter
@AllArgsConstructor
public class Token {

  protected final String value;
  protected final LocalDateTime expirationTime;

  public static Token fromTokenString(String tokenString) {
    String value = tokenString.split(",")[0];
    LocalDateTime expirationTime = LocalDateTime.parse(tokenString.split(",")[1]);
    return new Token(value, expirationTime) {
    };
  }

  public String toTokenString() {
    return value + "," + expirationTime;
  }
}
