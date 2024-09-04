package org.mju_likelion.festival.auth.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.auth.util.jwt.AuthenticationRole;
import org.mju_likelion.festival.auth.util.jwt.JwtUtil;
import org.mju_likelion.festival.auth.util.jwt.Payload;
import org.mju_likelion.festival.auth.util.rsa_key.RsaKeyStrategy;
import org.mju_likelion.festival.auth.util.rsa_key.manager.RsaKeyManager;
import org.mju_likelion.festival.auth.util.rsa_key.manager.RsaKeyManagerContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceUtil {

  private final RsaKeyManagerContext rsaKeyManagerContext;
  private final JwtUtil jwtUtil;

  public String createAccessToken(final UUID id, final AuthenticationRole role) {
    return jwtUtil.create(new Payload(id, role));
  }

  public RsaKeyManager getRsaKeyManager(final RsaKeyStrategy rsaKeyStrategy) {
    return rsaKeyManagerContext.rsaKeyManager(rsaKeyStrategy);
  }

  public RsaKeyManager getRsaKeyManager() {
    return rsaKeyManagerContext.rsaKeyManager();
  }
}
