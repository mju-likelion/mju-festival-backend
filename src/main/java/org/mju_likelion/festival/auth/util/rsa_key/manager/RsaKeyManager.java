package org.mju_likelion.festival.auth.util.rsa_key.manager;

import org.mju_likelion.festival.auth.util.rsa_key.RsaKey;
import org.mju_likelion.festival.auth.util.rsa_key.RsaKeyStrategy;

/**
 * RSA Key 관리자 인터페이스
 *
 * @implSpec RedisRsaKeyManager, TokenRsaKeyManager
 */
public interface RsaKeyManager {

  // Rsa Key 의 유효 시간 (초)
  long rsaKeyExpireSecond = 30;

  /**
   * RSA Key 를 생성한다.
   *
   * @return RSA Key
   */
  RsaKey generateRsaKey();


  /**
   * RSA Private Key 를 저장한 후, 해당 Private Key 를 식별할 수 있는 Key 를 반환한다.
   *
   * @param privateKey RSA Private Key
   * @return RSA Private Key 를 얻을 수 있는 Key
   */
  String savePrivateKey(final String privateKey);

  /**
   * Key 를 이용하여 암호화된 텍스트를 복호화한다.
   *
   * @param plainText 암호화된 텍스트
   * @param key       RSA Private Key 를 식별할 수 있는 Key
   * @return 복호화된 텍스트
   */
  String decryptByKey(final String plainText, final String key);

  /**
   * RsaKeyStrategy 를 반환한다.
   *
   * @return RsaKeyStrategy
   */
  RsaKeyStrategy rsaKeyStrategy();
}
