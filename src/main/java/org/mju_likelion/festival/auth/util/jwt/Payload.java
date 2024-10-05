package org.mju_likelion.festival.auth.util.jwt;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * JWT 토큰의 payload 부분을 나타내는 클래스.
 */
@Getter
@AllArgsConstructor
public class Payload {

  private UUID id;
  private AuthenticationRole role;
}
