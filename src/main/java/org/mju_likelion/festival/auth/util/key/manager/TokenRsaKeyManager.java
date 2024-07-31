package org.mju_likelion.festival.auth.util.key.manager;

import lombok.AllArgsConstructor;
import org.mju_likelion.festival.auth.domain.RsaKey;
import org.mju_likelion.festival.auth.domain.RsaKeyStrategy;
import org.mju_likelion.festival.auth.util.key.RsaKeyUtil;
import org.mju_likelion.festival.auth.util.token.CredentialTokenUtil;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenRsaKeyManager implements RsaKeyManager {

  private final RsaKeyUtil rsaKeyUtil;
  private final CredentialTokenUtil credentialTokenUtil;

  @Override
  public RsaKey generateRsaKey() {
    return rsaKeyUtil.generateRsaKey();
  }

  @Override
  public String savePrivateKey(String privateKey) {
    return credentialTokenUtil.getEncryptedCredentialToken(privateKey, this.rsaKeyExpireSecond);
  }

  @Override
  public String decryptByKey(String plainText, String key) {
    String privateKey = credentialTokenUtil.parsePrivateKey(key);
    return rsaKeyUtil.rsaDecode(plainText, privateKey);
  }

  @Override
  public RsaKeyStrategy rsaKeyStrategy() {
    return RsaKeyStrategy.TOKEN;
  }
}
