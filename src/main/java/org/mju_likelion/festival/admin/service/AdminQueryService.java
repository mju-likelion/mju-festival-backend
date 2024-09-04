package org.mju_likelion.festival.admin.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.ADMIN_NOT_FOUND_ERROR;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.mju_likelion.festival.common.exception.UnauthorizedException;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminQueryService {

  private final AdminJpaRepository adminJpaRepository;

  public void validateAdminExistence(final UUID adminId) {
    if (!adminJpaRepository.existsById(adminId)) {
      throw new NotFoundException(ADMIN_NOT_FOUND_ERROR);
    }
  }

  public Admin getExistingAdmin(final UUID adminId) {
    return adminJpaRepository.findById(adminId)
        .orElseThrow(() -> new NotFoundException(ADMIN_NOT_FOUND_ERROR));
  }

  public Admin getExistingAdmin(final String loginId, final String password) {
    return adminJpaRepository.findByLoginIdAndPassword(loginId, password)
        .orElseThrow(() -> new UnauthorizedException(ErrorType.INVALID_CREDENTIALS_ERROR));
  }
}
