package org.mju_likelion.festival.auth.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
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
   * Payload 를 받아 jwt 를 생성한다.
   *
   * @param payload Payload 정보
   * @return 생성된 토큰
   */
  public String create(final Payload payload) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .setSubject(payload.getId().toString())
        .claim("role", payload.getRole())
        .setIssuedAt(now)
        .setExpiration(expiration)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * 토큰을 받아 Payload를 반환한다.
   *
   * @param token 토큰
   * @return Payload 정보
   */
  public Payload getPayload(final String token) {
    try {
      Claims claims = Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody();

      UUID id = UUID.fromString(claims.getSubject());
      AuthenticationRole role = AuthenticationRole.valueOf(claims.get("role", String.class));
      return new Payload(id, role);
    } catch (Exception e) {
      throw new UnauthorizedException(ErrorType.INVALID_JWT_ERROR, e.getLocalizedMessage());
    }
  }
}
