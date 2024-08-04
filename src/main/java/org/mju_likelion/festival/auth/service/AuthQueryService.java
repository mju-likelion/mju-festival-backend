package org.mju_likelion.festival.auth.service;

import lombok.AllArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.auth.domain.RsaKey;
import org.mju_likelion.festival.auth.domain.RsaKeyStrategy;
import org.mju_likelion.festival.auth.dto.request.AdminLoginRequest;
import org.mju_likelion.festival.auth.dto.response.KeyResponse;
import org.mju_likelion.festival.auth.dto.response.LoginResponse;
import org.mju_likelion.festival.auth.util.jwt.AuthenticationRole;
import org.mju_likelion.festival.auth.util.jwt.JwtUtil;
import org.mju_likelion.festival.auth.util.jwt.Payload;
import org.mju_likelion.festival.auth.util.key.manager.RsaKeyManager;
import org.mju_likelion.festival.auth.util.key.manager.RsaKeyManagerContext;
import org.mju_likelion.festival.common.exception.UnauthorizedException;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthQueryService {

  private final RsaKeyManagerContext rsaKeyManagerContext;
  private final AdminJpaRepository adminJpaRepository;
  private final JwtUtil jwtUtil;

  public KeyResponse getKey() {
    RsaKeyManager rsaKeyManager = rsaKeyManagerContext.rsaKeyManager();

    RsaKey rsaKey = rsaKeyManager.generateRsaKey();

    String credentialKey = rsaKeyManager.savePrivateKey(rsaKey.privateKey());

    RsaKeyStrategy rsaKeyStrategy = rsaKeyManager.rsaKeyStrategy();

    return new KeyResponse(rsaKey.publicKey(), credentialKey, rsaKeyStrategy);
  }

  @Transactional(readOnly = true)
  public LoginResponse adminLogin(AdminLoginRequest adminLoginRequest,
      RsaKeyStrategy rsaKeyStrategy) {
    RsaKeyManager rsaKeyManager = rsaKeyManagerContext.rsaKeyManager(rsaKeyStrategy);

    String key = adminLoginRequest.getKey();
    String loginId = rsaKeyManager.decryptByKey(adminLoginRequest.getEncryptedLoginId(), key);
    String password = rsaKeyManager.decryptByKey(adminLoginRequest.getEncryptedPassword(), key);

    Admin admin = getExistingAdmin(loginId, password);

    String accessToken = jwtUtil.create(
        new Payload(admin.getId(), AuthenticationRole.from(admin.getRole())));
    return new LoginResponse(accessToken);
  }

  /**
   * 관리자가 존재하는지 확인하고, 존재하는 경우 사용자를 반환한다.
   *
   * @param loginId  로그인 ID
   * @param password 비밀번호
   * @return 관리자
   */
  private Admin getExistingAdmin(String loginId, String password) {
    return adminJpaRepository.findByLoginIdAndPassword(loginId, password)
        .orElseThrow(() -> new UnauthorizedException(ErrorType.INVALID_CREDENTIALS_ERROR));
  }
}
