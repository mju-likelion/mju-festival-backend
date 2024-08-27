package org.mju_likelion.festival.lost_item.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.ADMIN_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.LOST_ITEM_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.PAGE_OUT_OF_BOUND_ERROR;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.mju_likelion.festival.lost_item.domain.LostItem;
import org.mju_likelion.festival.lost_item.domain.repository.LostItemJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostItemServiceUtil {

  private final LostItemJpaRepository lostItemJpaRepository;
  private final AdminJpaRepository adminJpaRepository;

  public void validateAdminExistence(final UUID adminId) {
    if (!adminJpaRepository.existsById(adminId)) {
      throw new NotFoundException(ADMIN_NOT_FOUND_ERROR);
    }
  }

  public LostItem getExistLostItem(final UUID lostItemId) {
    return lostItemJpaRepository.findById(lostItemId)
        .orElseThrow(() -> new NotFoundException(LOST_ITEM_NOT_FOUND_ERROR));
  }

  public Admin getExistAdmin(final UUID studentCouncilId) {
    return adminJpaRepository.findById(studentCouncilId)
        .orElseThrow(() -> new NotFoundException(ADMIN_NOT_FOUND_ERROR));
  }

  public void validatePage(final int page, final int totalPage) {
    if (page != 0 && page >= totalPage) {
      throw new NotFoundException(PAGE_OUT_OF_BOUND_ERROR);
    }
  }
}
