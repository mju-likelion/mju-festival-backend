package org.mju_likelion.festival.auth.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.auth.util.jwt.AuthenticationRole;
import org.mju_likelion.festival.auth.util.jwt.JwtUtil;
import org.mju_likelion.festival.auth.util.jwt.Payload;
import org.mju_likelion.festival.auth.util.rsa_key.RsaKeyStrategy;
import org.mju_likelion.festival.auth.util.rsa_key.manager.RsaKeyManager;
import org.mju_likelion.festival.auth.util.rsa_key.manager.RsaKeyManagerContext;
import org.mju_likelion.festival.common.exception.UnauthorizedException;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.mju_likelion.festival.common.util.api.mju.MjuApiUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceUtil {

  private final RsaKeyManagerContext rsaKeyManagerContext;
  private final AdminJpaRepository adminJpaRepository;
  private final MjuApiUtil mjuApiUtil;
  private final JwtUtil jwtUtil;

  /**
   * 사용자의 학번과 비밀번호를 검증한다. 명지대학교 MSI API 를 이용하여 사용자의 존재 여부를 확인한다.
   *
   * @param studentId 학번
   * @param password  비밀번호
   */
  public void validateUser(final String studentId, final String password) {
    if (!mjuApiUtil.doUserCheck(studentId, password)) {
      throw new UnauthorizedException(ErrorType.INVALID_CREDENTIALS_ERROR);
    }
  }

  /**
   * 관리자가 존재하는지 확인하고, 존재하는 경우 사용자를 반환한다.
   *
   * @param loginId  로그인 ID
   * @param password 비밀번호
   * @return 관리자
   */
  public Admin getExistingAdmin(final String loginId, final String password) {
    return adminJpaRepository.findByLoginIdAndPassword(loginId, password)
        .orElseThrow(() -> new UnauthorizedException(ErrorType.INVALID_CREDENTIALS_ERROR));
  }

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
