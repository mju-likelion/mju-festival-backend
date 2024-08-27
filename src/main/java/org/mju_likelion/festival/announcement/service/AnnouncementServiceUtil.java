package org.mju_likelion.festival.announcement.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.ADMIN_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.ANNOUNCEMENT_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.PAGE_OUT_OF_BOUND_ERROR;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.announcement.domain.Announcement;
import org.mju_likelion.festival.announcement.domain.repository.AnnouncementJpaRepository;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnnouncementServiceUtil {

  private final AnnouncementJpaRepository announcementJpaRepository;
  private final AdminJpaRepository adminJpaRepository;

  public void validatePage(final int page, final int totalPage) {
    if (page != 0 && page >= totalPage) {
      throw new NotFoundException(PAGE_OUT_OF_BOUND_ERROR);
    }
  }

  public void validateAdminExistence(final UUID adminId) {
    if (!adminJpaRepository.existsById(adminId)) {
      throw new NotFoundException(ADMIN_NOT_FOUND_ERROR);
    }
  }

  public Admin getExistingAdmin(final UUID adminId) {
    return adminJpaRepository.findById(adminId)
        .orElseThrow(() -> new NotFoundException(ADMIN_NOT_FOUND_ERROR));
  }

  public Announcement getExistingAnnouncement(final UUID announcementId) {
    return announcementJpaRepository.findById(announcementId)
        .orElseThrow(() -> new NotFoundException(ANNOUNCEMENT_NOT_FOUND_ERROR));
  }
}
