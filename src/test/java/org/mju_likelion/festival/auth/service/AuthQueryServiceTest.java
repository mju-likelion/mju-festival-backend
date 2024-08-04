package org.mju_likelion.festival.auth.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.BDDMockito.willReturn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mju_likelion.festival.auth.domain.RsaKeyStrategy;
import org.mju_likelion.festival.auth.dto.request.AdminLoginRequest;
import org.mju_likelion.festival.auth.dto.response.KeyResponse;
import org.mju_likelion.festival.auth.dto.response.LoginResponse;
import org.mju_likelion.festival.auth.util.key.RsaKeyUtil;
import org.mju_likelion.festival.auth.util.key.manager.RedisRsaKeyManager;
import org.mju_likelion.festival.auth.util.key.manager.RsaKeyManagerContext;
import org.mju_likelion.festival.auth.util.key.manager.TokenRsaKeyManager;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("AuthQueryService")
@ApplicationTest
public class AuthQueryServiceTest {

  @Value("${admin-login-id}")
  private String adminLoginId;
  @Value("${admin-password}")
  private String adminPassword;
  @Autowired
  private AuthQueryService authQueryService;
  @MockBean
  private RsaKeyManagerContext rsaKeyManagerContext;
  @Autowired
  private RsaKeyUtil rsaKeyUtil;
  @Autowired
  private RedisRsaKeyManager redisRsaKeyManager;
  @Autowired
  private TokenRsaKeyManager tokenRsaKeyManager;

  @DisplayName("RedisRsaKeyManager 를 사용하여 암호화된 loginId, password, key를 받아 로그인한다.")
  @Test
  void testAdminLoginRedisRsaKeyManager() {
    // given
    willReturn(redisRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager();
    willReturn(redisRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager(RsaKeyStrategy.REDIS);
    KeyResponse keyResponse = authQueryService.getKey();
    AdminLoginRequest adminLoginRequest = createAdminLoginRequest(keyResponse);

    // when
    LoginResponse loginResponse = authQueryService.adminLogin(adminLoginRequest,
        RsaKeyStrategy.REDIS);

    // then
    assertNotNull(loginResponse.getAccessToken());
  }

  @DisplayName("TokenRsaKeyManager 를 사용하여 암호화된 loginId, password, key를 받아 로그인한다.")
  @Test
  void testAdminLoginWithTokenRsaKeyManager() {
    // given
    willReturn(tokenRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager();
    willReturn(tokenRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager(RsaKeyStrategy.TOKEN);
    KeyResponse keyResponse = authQueryService.getKey();
    AdminLoginRequest adminLoginRequest = createAdminLoginRequest(keyResponse);

    // when
    LoginResponse loginResponse = authQueryService.adminLogin(adminLoginRequest,
        RsaKeyStrategy.TOKEN);

    // then
    assertNotNull(loginResponse.getAccessToken());
  }

  /**
   * AdminLoginRequest 객체를 생성한다.
   *
   * @param keyResponse 키 응답 객체
   * @return AdminLoginRequest 객체
   */
  private AdminLoginRequest createAdminLoginRequest(KeyResponse keyResponse) {
    String publicKey = keyResponse.getRsaPublicKey();
    String credentialKey = keyResponse.getCredentialKey();

    String encryptedLoginId = rsaKeyUtil.rsaEncode(adminLoginId, publicKey);
    String encryptedPassword = rsaKeyUtil.rsaEncode(adminPassword, publicKey);

    return new AdminLoginRequest(encryptedLoginId, encryptedPassword,
        credentialKey);
  }
}
