package org.mju_likelion.festival.user.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.USER_NOT_FOUND_ERROR;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.mju_likelion.festival.common.exception.UnauthorizedException;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.mju_likelion.festival.common.util.api.mju.MjuApiUtil;
import org.mju_likelion.festival.stamp.service.StampQueryService;
import org.mju_likelion.festival.user.domain.User;
import org.mju_likelion.festival.user.domain.repository.UserJpaRepository;
import org.mju_likelion.festival.user.dto.response.StampResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

  private final UserJpaRepository userJpaRepository;
  private final MjuApiUtil mjuApiUtil;
  private final StampQueryService stampQueryService;

  public StampResponse getUserStampById(final UUID userId) {
    validateUserExists(userId);
    return stampQueryService.getStampResponseByUserId(userId);
  }

  public void validateUserExists(final UUID userId) {
    if (!userJpaRepository.existsById(userId)) {
      throw new NotFoundException(USER_NOT_FOUND_ERROR);
    }
  }

  /**
   * 사용자의 학번과 비밀번호를 검증한다. 명지대학교 MSI API 를 이용하여 사용자의 존재 여부를 확인한다.
   *
   * @param studentId 학번
   * @param password  비밀번호
   */
  public void validateUserExistsInMsi(final String studentId, final String password) {
    if (!mjuApiUtil.doUserCheck(studentId, password)) {
      throw new UnauthorizedException(ErrorType.INVALID_CREDENTIALS_ERROR);
    }
  }

  public User getExistingUser(final UUID userId) {
    return userJpaRepository.findById(userId).orElseThrow(
        () -> new NotFoundException(USER_NOT_FOUND_ERROR)
    );
  }

  public void saveUser(final User user) {
    userJpaRepository.save(user);
  }
}
