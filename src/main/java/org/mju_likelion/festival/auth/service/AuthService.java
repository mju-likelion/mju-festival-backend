package org.mju_likelion.festival.auth.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.auth.dto.request.UserLoginRequest;
import org.mju_likelion.festival.auth.dto.response.UserLoginResponse;
import org.mju_likelion.festival.auth.util.jwt.AuthenticationRole;
import org.mju_likelion.festival.auth.util.rsa_key.RsaKeyStrategy;
import org.mju_likelion.festival.auth.util.rsa_key.manager.RsaKeyManager;
import org.mju_likelion.festival.term.domain.Term;
import org.mju_likelion.festival.term.service.TermQueryService;
import org.mju_likelion.festival.user.service.UserQueryService;
import org.mju_likelion.festival.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

  private final TermQueryService termQueryService;
  private final UserService userService;
  private final UserQueryService userQueryService;
  private final AuthServiceUtil authServiceUtil;


  public UserLoginResponse userLogin(
      final UserLoginRequest userLoginRequest,
      final RsaKeyStrategy rsaKeyStrategy) {

    RsaKeyManager rsaKeyManager = authServiceUtil.getRsaKeyManager(rsaKeyStrategy);

    String key = userLoginRequest.getKey();
    String studentId = rsaKeyManager.decryptByKey(userLoginRequest.getEncryptedStudentId(), key);
    String password = rsaKeyManager.decryptByKey(userLoginRequest.getEncryptedPassword(), key);

    userQueryService.validateUserExistsInMsi(studentId, password);

    List<Term> terms = termQueryService.getValidTerms(userLoginRequest.getTerms());

    UUID userId = userService.saveOrGetUserId(studentId, terms);

    String accessToken = authServiceUtil.createAccessToken(userId, AuthenticationRole.USER);
    return UserLoginResponse.of(accessToken);
  }

  public void withdrawUser(final UUID userId) {
    userQueryService.validateUserExists(userId);
    userService.deleteById(userId);
  }
}
