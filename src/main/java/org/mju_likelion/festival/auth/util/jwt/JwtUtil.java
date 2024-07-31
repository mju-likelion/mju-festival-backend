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
