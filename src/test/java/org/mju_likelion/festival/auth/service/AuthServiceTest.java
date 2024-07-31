package org.mju_likelion.festival.auth.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.BDDMockito.willReturn;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mju_likelion.festival.auth.domain.RsaKeyStrategy;
import org.mju_likelion.festival.auth.dto.request.UserLoginRequest;
import org.mju_likelion.festival.auth.dto.response.KeyResponse;
import org.mju_likelion.festival.auth.dto.response.LoginResponse;
import org.mju_likelion.festival.auth.util.key.RsaKeyUtil;
import org.mju_likelion.festival.auth.util.key.manager.RedisRsaKeyManager;
import org.mju_likelion.festival.auth.util.key.manager.RsaKeyManagerContext;
import org.mju_likelion.festival.auth.util.key.manager.TokenRsaKeyManager;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.mju_likelion.festival.term.domain.Term;
import org.mju_likelion.festival.term.domain.repository.TermJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("AuthService")
@ApplicationTest
public class AuthServiceTest {

  @Value("${student-id}")
  private String studentId;
  @Value("${student-password}")
  private String studentPassword;
  @Autowired
  private AuthService authService;
  @MockBean
  private RsaKeyManagerContext rsaKeyManagerContext;
  @Autowired
  private RsaKeyUtil rsaKeyUtil;
  @Autowired
  private RedisRsaKeyManager redisRsaKeyManager;
  @Autowired
  private TokenRsaKeyManager tokenRsaKeyManager;
  @Autowired
  private TermJpaRepository termJpaRepository;


  @DisplayName("RedisRsaKeyManager 를 사용하여 암호화된 studentId, password, key를 받아 로그인한다.")
  @Test
  void testUserLogin() {
    // given
    willReturn(redisRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager();
    willReturn(redisRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager(RsaKeyStrategy.REDIS);
    KeyResponse keyResponse = authService.getKey();
    UserLoginRequest userLoginRequest = createUserLoginRequest(keyResponse);

    // when
    LoginResponse loginResponse = authService.userLogin(userLoginRequest,
        RsaKeyStrategy.REDIS);

    // then
    assertNotNull(loginResponse.getAccessToken());
  }

  @DisplayName("TokenRsaKeyManager 를 사용하여 암호화된 studentId, password, key를 받아 로그인한다.")
  @Test
  void testUserLoginWithTokenRsaKeyManager() {
    // given
    willReturn(tokenRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager();
    willReturn(tokenRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager(RsaKeyStrategy.TOKEN);
    KeyResponse keyResponse = authService.getKey();
    UserLoginRequest userLoginRequest = createUserLoginRequest(keyResponse);

    // when
    LoginResponse loginResponse = authService.userLogin(userLoginRequest,
        RsaKeyStrategy.TOKEN);

    // then
    assertNotNull(loginResponse.getAccessToken());
  }

  /**
   * UserLoginRequest 객체를 생성한다.
   *
   * @param keyResponse 키 응답 객체
   * @return UserLoginRequest 객체
   */
  private UserLoginRequest createUserLoginRequest(KeyResponse keyResponse) {
    String publicKey = keyResponse.getRsaPublicKey();
    String credentialKey = keyResponse.getCredentialKey();

    String encryptedStudentId = rsaKeyUtil.rsaEncode(studentId, publicKey);
    String encryptedPassword = rsaKeyUtil.rsaEncode(studentPassword, publicKey);

    List<Term> terms = termJpaRepository.findAll();
    HashMap<UUID, Boolean> termMap = new HashMap<>();
    terms.forEach(term -> termMap.put(term.getId(), true));

    return new UserLoginRequest(encryptedStudentId, encryptedPassword,
        credentialKey, termMap);
  }
}
