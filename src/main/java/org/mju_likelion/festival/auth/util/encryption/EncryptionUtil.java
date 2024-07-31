package org.mju_likelion.festival.auth.util.encryption;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.mju_likelion.festival.common.exception.InternalServerException;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 암호화 유틸리티 클래스 대칭 키 알고리즘을 사용하여 주어진 문자열을 암호화하거나 복호화한다.
 */
@Service
public class EncryptionUtil {

  private static final String ALGORITHM = "AES";
  private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
  private final SecretKey secretKey;
  private final IvParameterSpec ivParameterSpec;

  public EncryptionUtil(
      @Value("${security.encryption.secret-key}") final String base64EncodedKey,
      @Value("${security.encryption.iv}") final String base64EncodedIv) {

    byte[] decodedKey = Base64.getDecoder().decode(base64EncodedKey);
    this.secretKey = new SecretKeySpec(decodedKey, ALGORITHM);
    byte[] decodedIv = Base64.getDecoder().decode(base64EncodedIv);
    this.ivParameterSpec = new IvParameterSpec(decodedIv);
  }

  /**
   * 주어진 문자열을 대칭키로 암호화한다.
   *
   * @param plainText 암호화할 문자열
   * @return 암호화된 문자열
   */
  public String encrypt(String plainText) {
    try {
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
      byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(encryptedBytes);
    } catch (Exception e) {
      throw new InternalServerException(ErrorType.ENCRYPT_ERROR, e.getMessage());
    }
  }

  /**
   * 주어진 암호화된 문자열을 대칭키로 복호화한다.
   *
   * @param encryptedText 복호화할 문자열
   * @return 복호화된 문자열
   */
  public String decrypt(String encryptedText) {
    try {
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
      byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
      byte[] decryptedBytes = cipher.doFinal(decodedBytes);
      return new String(decryptedBytes, StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new InternalServerException(ErrorType.DECRYPT_ERROR, e.getMessage());
    }
  }
}
