package org.mju_likelion.festival.auth.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.auth.domain.RsaKey;
import org.mju_likelion.festival.auth.domain.RsaKeyStrategy;
import org.mju_likelion.festival.auth.dto.request.AdminLoginRequest;
import org.mju_likelion.festival.auth.dto.request.UserLoginRequest;
import org.mju_likelion.festival.auth.dto.response.KeyResponse;
import org.mju_likelion.festival.auth.dto.response.LoginResponse;
import org.mju_likelion.festival.auth.dto.response.TermResponse;
import org.mju_likelion.festival.auth.util.jwt.JwtUtil;
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
  private final AdminJpaRepository adminJpaRepository;
  private final JwtUtil jwtUtil;

  public KeyResponse getKey() {
    RsaKeyManager rsaKeyManager = rsaKeyManager();

    RsaKey rsaKey = rsaKeyManager.generateRsaKey();

    String credentialKey = rsaKeyManager.savePrivateKey(rsaKey.privateKey());

    RsaKeyStrategy rsaKeyStrategy = rsaKeyManager.rsaKeyStrategy();

    return new KeyResponse(rsaKey.publicKey(), credentialKey, rsaKeyStrategy);
  }

  @Transactional
  public LoginResponse userLogin(UserLoginRequest userLoginRequest, RsaKeyStrategy rsaKeyStrategy) {
    RsaKeyManager rsaKeyManager = rsaKeyManager(rsaKeyStrategy);

    String key = userLoginRequest.getKey();
    String studentId = rsaKeyManager.decryptByKey(userLoginRequest.getEncryptedStudentId(), key);
    String password = rsaKeyManager.decryptByKey(userLoginRequest.getEncryptedPassword(), key);

    validateUser(studentId, password);

    UUID userId = saveOrGetUserId(studentId, userLoginRequest.getTerms());

    String accessToken = jwtUtil.create(userId.toString());
    return new LoginResponse(accessToken);
  }

  @Transactional(readOnly = true)
  public LoginResponse adminLogin(AdminLoginRequest adminLoginRequest,
      RsaKeyStrategy rsaKeyStrategy) {
    RsaKeyManager rsaKeyManager = rsaKeyManager(rsaKeyStrategy);

    String key = adminLoginRequest.getKey();
    String loginId = rsaKeyManager.decryptByKey(adminLoginRequest.getEncryptedLoginId(), key);
    String password = rsaKeyManager.decryptByKey(adminLoginRequest.getEncryptedPassword(), key);

    Admin admin = getExistingAdmin(loginId, password);

    String accessToken = jwtUtil.create(admin.getId().toString());
    return new LoginResponse(accessToken);
  }

  @Transactional(readOnly = true)
  public List<TermResponse> getTerms() {
    return termJpaRepository.findTermsByOrderBySequenceAsc().stream()
        .map(TermResponse::of)
        .collect(Collectors.toList());
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
        .orElseThrow(() -> new UnauthorizedException(ErrorType.INVALID_CREDENTIALS));
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
  private UUID saveOrGetUserId(String studentId, Map<UUID, Boolean> terms) {
    Optional<UUID> userId = userJpaRepository.findIdByStudentId(studentId);

    if (userId.isPresent()) {
      return userId.get();
    }

    List<Term> validTerms = getValidTerms(terms.keySet());
    return saveUser(studentId, validTerms);
  }

  /**
   * 사용자를 저장하고, 사용자의 ID를 반환한다.
   *
   * @param studentId 학번
   * @param terms     사용자가 동의한 약관
   * @return 사용자의 ID
   */
  private UUID saveUser(String studentId, List<Term> terms) {
    User user = new User(studentId);

    terms.forEach(user::agreeToTerm);

    userJpaRepository.save(user);

    return user.getId();
  }

  /**
   * 약관 ID 목록을 이용하여 유효한 약관 목록을 반환한다.
   *
   * @param termIds 약관 ID 목록
   * @return 유효한 약관 목록
   */
  private List<Term> getValidTerms(Set<UUID> termIds) {
    List<Term> terms = termJpaRepository.findAll();
    validateTermIds(terms, termIds);
    return terms;
  }

  /**
   * 약관 ID 목록이 유효한지 검증한다.
   *
   * @param termsInDb DB 의 약관 목록
   * @param termIds   약관 ID 목록
   */
  private void validateTermIds(List<Term> termsInDb, Set<UUID> termIds) {
    Set<UUID> validTermIds = termsInDb.stream().map(Term::getId).collect(Collectors.toSet());
    if (!validTermIds.containsAll(termIds)) {
      throw new BadRequestException(ErrorType.MISSING_TERM);
    }
  }

  /**
   * 사용자의 학번과 비밀번호를 검증한다. 명지대학교 MSI API 를 이용하여 사용자의 존재 여부를 확인한다.
   *
   * @param studentId 학번
   * @param password  비밀번호
   */
  private void validateUser(String studentId, String password) {
    if (!mjuApiUtil.doUserCheck(studentId, password)) {
      throw new UnauthorizedException(ErrorType.INVALID_CREDENTIALS);
    }
  }

  private RsaKeyManager rsaKeyManager() {
    return rsaKeyManagerContext.rsaKeyManager();
  }

  private RsaKeyManager rsaKeyManager(RsaKeyStrategy rsaKeyStrategy) {
    return rsaKeyManagerContext.rsaKeyManager(rsaKeyStrategy);
  }
}
