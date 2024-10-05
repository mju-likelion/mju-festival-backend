package org.mju_likelion.festival.auth.service;

import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.service.AdminQueryService;
import org.mju_likelion.festival.auth.dto.request.AdminLoginRequest;
import org.mju_likelion.festival.auth.dto.response.AdminLoginResponse;
import org.mju_likelion.festival.auth.dto.response.KeyResponse;
import org.mju_likelion.festival.auth.util.jwt.AuthenticationRole;
import org.mju_likelion.festival.auth.util.rsa_key.RsaKey;
import org.mju_likelion.festival.auth.util.rsa_key.RsaKeyStrategy;
import org.mju_likelion.festival.auth.util.rsa_key.manager.RsaKeyManager;
import org.mju_likelion.festival.common.exception.UnauthorizedException;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthQueryService {

  private final AdminQueryService adminQueryService;
  private final AuthServiceUtil authServiceUtil;
  private final PasswordEncoder passwordEncoder;

  public KeyResponse getKey() {
    RsaKeyManager rsaKeyManager = authServiceUtil.getRsaKeyManager();

    RsaKey rsaKey = rsaKeyManager.generateRsaKey();

    String credentialKey = rsaKeyManager.savePrivateKey(rsaKey.privateKey());

    RsaKeyStrategy rsaKeyStrategy = rsaKeyManager.rsaKeyStrategy();

    return new KeyResponse(rsaKey.publicKey(), credentialKey, rsaKeyStrategy);
  }

  public AdminLoginResponse adminLogin(
      final AdminLoginRequest adminLoginRequest,
      final RsaKeyStrategy rsaKeyStrategy) {

    RsaKeyManager rsaKeyManager = authServiceUtil.getRsaKeyManager(rsaKeyStrategy);

    String key = adminLoginRequest.getKey();
    String loginId = rsaKeyManager.decryptByKey(adminLoginRequest.getEncryptedLoginId(), key);
    String plainPassword = rsaKeyManager.decryptByKey(adminLoginRequest.getEncryptedPassword(),
        key);

    Admin admin = adminQueryService.getExistingAdmin(loginId);

    validateAdminPassword(admin, plainPassword);

    String accessToken = authServiceUtil.createAccessToken(admin.getId(),
        AuthenticationRole.from(admin.getRole()));

    return AdminLoginResponse.of(accessToken, admin.getRole());
  }

  private void validateAdminPassword(final Admin admin, final String plainPassword) {
    if (!passwordEncoder.matches(plainPassword, admin.getPassword())) {
      throw new UnauthorizedException(ErrorType.INVALID_CREDENTIALS_ERROR);
    }
  }
}
