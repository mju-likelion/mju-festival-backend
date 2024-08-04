package org.mju_likelion.festival.common.util.token;

import static org.mju_likelion.festival.common.exception.type.ErrorType.TOKEN_EXPIRED_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.TOKEN_GENERATE_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.TOKEN_PARSE_ERROR;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.auth.util.encryption.EncryptionUtil;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.common.exception.InternalServerException;
import org.springframework.stereotype.Component;

/**
 * Token 관련 유틸리티
 */
@Component
@AllArgsConstructor
public class TokenUtil {

  private final EncryptionUtil encryptionUtil;

  /**
   * Token 을 암호화하여 반환
   *
   * @param value                  value 문자열
   * @param tokenExpirationSeconds 토큰 만료 시간
   * @return 암호화된 Token
   */
  public String getEncryptedToken(String value, long tokenExpirationSeconds) {
    try {
      Token token = generateToken(value, tokenExpirationSeconds);
      return encryptionUtil.encrypt(token.toTokenString());
    } catch (Exception e) {
      throw new InternalServerException(TOKEN_GENERATE_ERROR, e.getMessage());
    }
  }

  /**
   * Token 생성
   *
   * @param value                  value 문자열
   * @param tokenExpirationSeconds 토큰 만료 시간
   * @return Token
   */
  private Token generateToken(String value, long tokenExpirationSeconds) {
    return new Token(value,
        LocalDateTime.now().plusSeconds(tokenExpirationSeconds));
  }

  /**
   * 암호화된 토큰을 복호화하여 value 반환
   *
   * @param encryptedToken 암호화된 토큰
   * @return value
   */
  public String parseValue(String encryptedToken) {
    try {
      String decryptedToken = encryptionUtil.decrypt(encryptedToken);
      Token token = Token.fromTokenString(decryptedToken);
      validateToken(token);

      return token.getValue();
    } catch (Exception e) {
      throw new InternalServerException(TOKEN_PARSE_ERROR, e.getMessage());
    }
  }

  /**
   * Token 만료 여부 확인
   *
   * @param token Token
   */
  private void validateToken(Token token) {
    if (token.getExpirationTime().isBefore(LocalDateTime.now())) {
      throw new BadRequestException(TOKEN_EXPIRED_ERROR);
    }
  }
}
