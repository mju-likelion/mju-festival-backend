package org.mju_likelion.festival.auth.util.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.mju_likelion.festival.common.exception.UnauthorizedException;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

  private final SecretKey key;
  private final long validityInMilliseconds;

  public JwtUtil(@Value("${security.jwt.token.secret-key}") final String secretKey,
      @Value("${security.jwt.token.expire-length}") final long validityInMilliseconds) {
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    this.validityInMilliseconds = validityInMilliseconds;
  }

  /**
   * payload를 받아 토큰을 생성한다.
   *
   * @param payload 토큰에 담을 정보
   * @return 생성된 토큰
   */
  public String create(final String payload) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .setSubject(payload)
        .setIssuedAt(now)
        .setExpiration(expiration)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * 토큰을 받아 payload를 반환한다.
   *
   * @param token 토큰
   * @return payload
   */
  public String getPayload(final String token) {
    try {
      return Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getSubject();
    } catch (JwtException e) {
      throw new UnauthorizedException(ErrorType.INVALID_JWT, e.getLocalizedMessage());
    }
  }
}
