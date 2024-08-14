package org.mju_likelion.festival.auth.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.auth.domain.RsaKeyStrategy;
import org.mju_likelion.festival.auth.dto.request.UserLoginRequest;
import org.mju_likelion.festival.auth.dto.response.LoginResponse;
import org.mju_likelion.festival.auth.util.jwt.AuthenticationRole;
import org.mju_likelion.festival.auth.util.jwt.JwtUtil;
import org.mju_likelion.festival.auth.util.jwt.Payload;
import org.mju_likelion.festival.auth.util.key.manager.RsaKeyManager;
import org.mju_likelion.festival.auth.util.key.manager.RsaKeyManagerContext;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.common.exception.UnauthorizedException;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.mju_likelion.festival.common.util.api.MjuApiUtil;
import org.mju_likelion.festival.term.domain.Term;
import org.mju_likelion.festival.term.domain.repository.TermJpaRepository;
import org.mju_likelion.festival.user.domain.User;
import org.mju_likelion.festival.user.domain.repository.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthService {

  private final RsaKeyManagerContext rsaKeyManagerContext;
  private final MjuApiUtil mjuApiUtil;
  private final UserJpaRepository userJpaRepository;
  private final TermJpaRepository termJpaRepository;
  private final JwtUtil jwtUtil;

  @Transactional
  public LoginResponse userLogin(
      final UserLoginRequest userLoginRequest,
      final RsaKeyStrategy rsaKeyStrategy) {

    RsaKeyManager rsaKeyManager = rsaKeyManagerContext.rsaKeyManager(rsaKeyStrategy);

    String key = userLoginRequest.getKey();
    String studentId = rsaKeyManager.decryptByKey(userLoginRequest.getEncryptedStudentId(), key);
    String password = rsaKeyManager.decryptByKey(userLoginRequest.getEncryptedPassword(), key);

    validateUser(studentId, password);

    UUID userId = saveOrGetUserId(studentId, userLoginRequest.getTerms());

    String accessToken = jwtUtil.create(new Payload(userId, AuthenticationRole.USER));
    return new LoginResponse(accessToken);
  }

  /**
   * 사용자가 동의한 약관을 저장하고, 사용자의 ID를 반환한다.
   * <p>
   * 사용자가 이미 존재하는 경우, 사용자의 ID를 반환한다.
   *
   * @param studentId 학번
   * @param terms     사용자가 동의한 약관
   * @return 사용자의 ID
   */
  private UUID saveOrGetUserId(final String studentId, final Map<UUID, Boolean> terms) {
    List<Term> validTerms = getValidTerms(terms);

    Optional<UUID> userId = userJpaRepository.findIdByStudentId(studentId);

    return userId.orElseGet(() -> saveUser(studentId, validTerms));
  }

  /**
   * 사용자를 저장하고, 사용자의 ID를 반환한다.
   *
   * @param studentId 학번
   * @param terms     사용자가 동의한 약관
   * @return 사용자의 ID
   */
  private UUID saveUser(final String studentId, final List<Term> terms) {
    User user = new User(studentId);

    terms.forEach(user::agreeToTerm);

    userJpaRepository.save(user);

    return user.getId();
  }

  /**
   * 약관 ID 목록을 이용하여 유효한 약관 목록을 반환한다.
   *
   * @param terms 사용자가 동의한 약관
   * @return 유효한 약관 목록
   */
  private List<Term> getValidTerms(final Map<UUID, Boolean> terms) {
    List<Term> termsInDb = termJpaRepository.findAll();
    validateTermIds(termsInDb, terms.keySet());
    return termsInDb;
  }

  /**
   * 약관 ID 목록이 유효한지 검증한다.
   *
   * @param termsInDb DB 의 약관 목록
   * @param termIds   약관 ID 목록
   */
  private void validateTermIds(final List<Term> termsInDb, final Set<UUID> termIds) {
    Set<UUID> validTermIds = termsInDb.stream().map(Term::getId).collect(Collectors.toSet());
    if (!termIds.containsAll(validTermIds)) {
      throw new BadRequestException(ErrorType.MISSING_TERM_ERROR);
    }
  }

  /**
   * 사용자의 학번과 비밀번호를 검증한다. 명지대학교 MSI API 를 이용하여 사용자의 존재 여부를 확인한다.
   *
   * @param studentId 학번
   * @param password  비밀번호
   */
  private void validateUser(final String studentId, final String password) {
    if (!mjuApiUtil.doUserCheck(studentId, password)) {
      throw new UnauthorizedException(ErrorType.INVALID_CREDENTIALS_ERROR);
    }
  }
}
