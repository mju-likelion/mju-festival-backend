package org.mju_likelion.festival.auth.util.key;

import static org.mju_likelion.festival.common.exception.type.ErrorType.RSA_KEY_ERROR;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import org.mju_likelion.festival.common.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RsaKeyUtil {

  private final String instanceType = "RSA";
  private final int keySize;

  public RsaKeyUtil(@Value("${key.size}") int keySize) {
    this.keySize = keySize;
  }

  /**
   * RSAKey 를 생성한다.
   *
   * @return RSAKey
   */
  public RsaKey generateRsaKey() {
    KeyPair keyPair = generateKeypair();

    String publicKey = base64EncodeToString(keyPair.getPublic().getEncoded());
    String privateKey = base64EncodeToString(keyPair.getPrivate().getEncoded());

    return new RsaKey(publicKey, privateKey);
  }

  /**
   * RSA KeyPair 를 생성한다.
   *
   * @return RSA KeyPair
   */
  private KeyPair generateKeypair() {
    try {
      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(instanceType);
      keyPairGen.initialize(keySize, new SecureRandom());

      return keyPairGen.genKeyPair();
    } catch (Exception e) {
      throw new InternalServerException(RSA_KEY_ERROR, e.getMessage());
    }
  }

  /**
   * RSA 암호화, Public Key 로 plainText 를 암호화한다.
   *
   * @param plainText 암호화할 문자열
   * @param publicKey RSA Public Key
   * @return 암호화된 문자열
   * @deprecated Test 에서만 사용한다.
   */
  public String rsaEncode(final String plainText, final String publicKey) {
    try {
      Cipher cipher = Cipher.getInstance(instanceType);
      cipher.init(Cipher.ENCRYPT_MODE, convertPublicKey(publicKey));

      byte[] plainTextByte = cipher.doFinal(plainText.getBytes());

      return base64EncodeToString(plainTextByte);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * RSA 복호화, Private Key 로 암호화된 문자열을 복호화한다.
   *
   * @param encryptedPlainText 암호화된 문자열
   * @param privateKey         RSA Private Key
   * @return 복호화된 문자열
   */
  public String rsaDecode(final String encryptedPlainText, final String privateKey) {
    try {
      byte[] encryptedPlainTextByte = Base64.getDecoder().decode(encryptedPlainText.getBytes());

      Cipher cipher = Cipher.getInstance(instanceType);
      cipher.init(Cipher.DECRYPT_MODE, convertPrivateKey(privateKey));

      return new String(cipher.doFinal(encryptedPlainTextByte));
    } catch (Exception e) {
      throw new InternalServerException(RSA_KEY_ERROR, e.getMessage());
    }
  }

  /**
   * String 형태의 PublicKey 를 PublicKey 객체로 변환한다.
   *
   * @param publicKey String 형태의 PublicKey
   * @return PublicKey 객체
   * @deprecated Test 에서만 사용한다.
   */
  public PublicKey convertPublicKey(final String publicKey) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(instanceType);
      byte[] publicKeyByte = Base64.getDecoder().decode(publicKey.getBytes());

      return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyByte));
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * String 형태의 PrivateKey 를 PrivateKey 객체로 변환한다.
   *
   * @param privateKey String 형태의 PrivateKey
   * @return PrivateKey 객체
   */
  public PrivateKey convertPrivateKey(final String privateKey) {
    try {
      KeyFactory keyFactory = KeyFactory.getInstance(instanceType);
      byte[] privateKeyByte = Base64.getDecoder().decode(privateKey.getBytes());

      return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyByte));
    } catch (Exception e) {
      throw new InternalServerException(RSA_KEY_ERROR, e.getMessage());
    }
  }

  private String base64EncodeToString(final byte[] byteData) {
    return Base64.getEncoder().encodeToString(byteData);
  }
}
