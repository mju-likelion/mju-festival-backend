package org.mju_likelion.festival.auth.util.token;

import static org.mju_likelion.festival.common.exception.type.ErrorType.CREDENTIAL_TOKEN_EXPIRED_ERROR;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.auth.util.encryption.EncryptionUtil;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CredentialTokenUtil {

  private final EncryptionUtil encryptionUtil;

  /**
   * CredentialToken 을 암호화하여 반환
   *
   * @param privateKey             privateKey 문자열
   * @param tokenExpirationSeconds 토큰 만료 시간
   * @return 암호화된 CredentialToken
   */
  public String getEncryptedCredentialToken(String privateKey, long tokenExpirationSeconds) {
    CredentialToken credentialToken = generateToken(privateKey, tokenExpirationSeconds);
    return encryptionUtil.encrypt(credentialToken.toTokenString());
  }

  /**
   * CredentialToken 생성
   *
   * @param privateKey             privateKey 문자열
   * @param tokenExpirationSeconds 토큰 만료 시간
   * @return CredentialToken
   */
  private CredentialToken generateToken(String privateKey, long tokenExpirationSeconds) {
    return new CredentialToken(privateKey, LocalDateTime.now().plusSeconds(tokenExpirationSeconds));
  }

  /**
   * 암호화된 토큰을 복호화하여 privateKey 반환
   *
   * @param encryptedToken 암호화된 토큰
   * @return privateKey
   */
  public String parsePrivateKey(String encryptedToken) {
    String decryptedToken = encryptionUtil.decrypt(encryptedToken);
    CredentialToken credentialToken = CredentialToken.fromTokenString(decryptedToken);
    validateCredentialToken(credentialToken);

    return credentialToken.getPrivateKey();
  }

  /**
   * CredentialToken 만료 여부 확인
   *
   * @param credentialToken CredentialToken
   */
  private void validateCredentialToken(CredentialToken credentialToken) {
    if (credentialToken.getExpirationTime().isBefore(LocalDateTime.now())) {
      throw new BadRequestException(CREDENTIAL_TOKEN_EXPIRED_ERROR);
    }
  }
}
