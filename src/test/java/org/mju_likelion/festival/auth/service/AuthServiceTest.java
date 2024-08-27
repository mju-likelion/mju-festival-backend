package org.mju_likelion.festival.auth.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willReturn;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mju_likelion.festival.auth.dto.request.UserLoginRequest;
import org.mju_likelion.festival.auth.dto.response.KeyResponse;
import org.mju_likelion.festival.auth.dto.response.UserLoginResponse;
import org.mju_likelion.festival.auth.util.rsa_key.RsaKeyStrategy;
import org.mju_likelion.festival.auth.util.rsa_key.RsaKeyUtil;
import org.mju_likelion.festival.auth.util.rsa_key.manager.RedisRsaKeyManager;
import org.mju_likelion.festival.auth.util.rsa_key.manager.RsaKeyManagerContext;
import org.mju_likelion.festival.auth.util.rsa_key.manager.TokenRsaKeyManager;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.mju_likelion.festival.common.exception.BadRequestException;
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
  @Autowired
  private TermJpaRepository termJpaRepository;


  @DisplayName("RedisRsaKeyManager 를 사용하여 암호화된 studentId, password, key를 받아 로그인한다.")
  @Test
  void testUserLoginRedisRsaKeyManager() {
    // given
    willReturn(redisRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager();
    willReturn(redisRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager(RsaKeyStrategy.REDIS);
    KeyResponse keyResponse = authQueryService.getKey();
    UserLoginRequest userLoginRequest = createUserLoginRequest(keyResponse);

    // when
    UserLoginResponse userLoginResponse = authService.userLogin(userLoginRequest,
        RsaKeyStrategy.REDIS);

    // then
    assertNotNull(userLoginResponse.getAccessToken());
  }

  @DisplayName("TokenRsaKeyManager 를 사용하여 암호화된 studentId, password, key를 받아 로그인한다.")
  @Test
  void testUserLoginWithTokenRsaKeyManager() {
    // given
    willReturn(tokenRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager();
    willReturn(tokenRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager(RsaKeyStrategy.TOKEN);
    KeyResponse keyResponse = authQueryService.getKey();
    UserLoginRequest userLoginRequest = createUserLoginRequest(keyResponse);

    // when
    System.out.println("-----------------");
    UserLoginResponse userLoginResponse = authService.userLogin(userLoginRequest,
        RsaKeyStrategy.TOKEN);

    // then
    assertNotNull(userLoginResponse.getAccessToken());
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

  @DisplayName("TokenRsaKeyManager 를 사용하여 동의 약관이 누락된 경우 로그인에 실패한다.")
  @Test
  void testUserLoginTokenRsaKeyManagerWithMissingTerms() {
    // given
    willReturn(tokenRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager();
    willReturn(tokenRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager(RsaKeyStrategy.TOKEN);
    KeyResponse keyResponse = authQueryService.getKey();
    UserLoginRequest userLoginRequest = createUserLoginRequest(keyResponse);
    userLoginRequest.getTerms().keySet().stream().findFirst()
        .ifPresent(uuid -> userLoginRequest.getTerms().remove(uuid));

    // when & then
    assertThrows(BadRequestException.class,
        () -> authService.userLogin(userLoginRequest,
            RsaKeyStrategy.TOKEN));
  }

  @DisplayName("RedisRsaKeyManager 를 사용하여 동의 약관이 누락된 경우 로그인에 실패한다.")
  @Test
  void testUserLoginRedisRsaKeyManagerWithMissingTerms() {
    // given
    willReturn(redisRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager();
    willReturn(redisRsaKeyManager).given(rsaKeyManagerContext).rsaKeyManager(RsaKeyStrategy.REDIS);
    KeyResponse keyResponse = authQueryService.getKey();
    UserLoginRequest userLoginRequest = createUserLoginRequest(keyResponse);
    userLoginRequest.getTerms().keySet().stream().findFirst()
        .ifPresent(uuid -> userLoginRequest.getTerms().remove(uuid));

    // when & then
    assertThrows(BadRequestException.class,
        () -> authService.userLogin(userLoginRequest,
            RsaKeyStrategy.REDIS));
  }
}
