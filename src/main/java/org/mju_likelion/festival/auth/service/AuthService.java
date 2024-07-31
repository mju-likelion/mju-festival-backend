package org.mju_likelion.festival.auth.service;

import lombok.AllArgsConstructor;
import org.mju_likelion.festival.auth.domain.RsaKey;
import org.mju_likelion.festival.auth.domain.RsaKeyStrategy;
import org.mju_likelion.festival.auth.dto.response.KeyResponse;
import org.mju_likelion.festival.auth.util.key.manager.RsaKeyManager;
import org.mju_likelion.festival.auth.util.key.manager.RsaKeyManagerContext;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

  private final RsaKeyManagerContext rsaKeyManagerContext;

  public KeyResponse getKey() {
    RsaKeyManager rsaKeyManager = rsaKeyManager();

    RsaKey rsaKey = rsaKeyManager.generateRsaKey();

    String credentialKey = rsaKeyManager.savePrivateKey(rsaKey.privateKey());

    RsaKeyStrategy rsaKeyStrategy = rsaKeyManager.rsaKeyStrategy();

    return new KeyResponse(rsaKey.publicKey(), credentialKey, rsaKeyStrategy);
  }

  private RsaKeyManager rsaKeyManager() {
    return rsaKeyManagerContext.rsaKeyManager();
  }

  private RsaKeyManager rsaKeyManager(RsaKeyStrategy rsaKeyStrategy) {
    return rsaKeyManagerContext.rsaKeyManager(rsaKeyStrategy);
  }
}
