package org.mju_likelion.festival.auth.util.rsa_key.manager;

import lombok.AllArgsConstructor;
import org.mju_likelion.festival.auth.util.rsa_key.RsaKey;
import org.mju_likelion.festival.auth.util.rsa_key.RsaKeyStrategy;
import org.mju_likelion.festival.auth.util.rsa_key.RsaKeyUtil;
import org.mju_likelion.festival.common.util.token.TokenUtil;
import org.springframework.stereotype.Service;

/**
 * 토큰을 이용한 RsaKeyManager
 *
 * @see RsaKeyManager
 */
@Service
@AllArgsConstructor
public class TokenRsaKeyManager implements RsaKeyManager {

  private final RsaKeyUtil rsaKeyUtil;
  private final TokenUtil tokenUtil;

  @Override
  public RsaKey generateRsaKey() {
    return rsaKeyUtil.generateRsaKey();
  }

  @Override
  public String savePrivateKey(final String privateKey) {
    return tokenUtil.getEncryptedToken(privateKey, this.rsaKeyExpireSecond);
  }

  @Override
  public String decryptByKey(final String encryptedText, final String key) {
    String privateKey = tokenUtil.parseValue(key);
    return rsaKeyUtil.rsaDecode(encryptedText, privateKey);
  }

  @Override
  public RsaKeyStrategy rsaKeyStrategy() {
    return RsaKeyStrategy.TOKEN;
  }
}
