package org.mju_likelion.festival.user.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.booth.service.BoothUserService;
import org.mju_likelion.festival.term.domain.Term;
import org.mju_likelion.festival.term.service.TermUserService;
import org.mju_likelion.festival.user.domain.User;
import org.mju_likelion.festival.user.domain.repository.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

  private final UserJpaRepository userJpaRepository;
  private final UserQueryService userQueryService;

  private final TermUserService termUserService;
  private final BoothUserService boothUserService;

  public void withdrawUser(final UUID userId) {
    userQueryService.validateUserExists(userId);
    deleteUserById(userId);
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
  public UUID saveOrGetUserId(final String studentId, final List<Term> terms) {
    Optional<UUID> userId = userJpaRepository.findIdByStudentId(studentId);

    return userId.orElseGet(() -> saveUser(studentId, terms));
  }

  private void deleteUserById(final UUID userId) {
    termUserService.deleteAllByUserId(userId);
    boothUserService.deleteAllByUserId(userId);
    userJpaRepository.deleteById(userId);
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
}
