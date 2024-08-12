package org.mju_likelion.festival.announcement.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.ADMIN_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.ANNOUNCEMENT_NOT_FOUND_ERROR;

import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.announcement.domain.Announcement;
import org.mju_likelion.festival.announcement.domain.repository.AnnouncementJpaRepository;
import org.mju_likelion.festival.announcement.dto.request.CreateAnnouncementRequest;
import org.mju_likelion.festival.announcement.dto.request.UpdateAnnouncementRequest;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.mju_likelion.festival.image.domain.Image;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class AnnouncementService {

  private final AnnouncementJpaRepository announcementJpaRepository;
  private final AdminJpaRepository adminJpaRepository;

  public void createAnnouncement(CreateAnnouncementRequest createAnnouncementRequest,
      UUID adminId) {

    Admin admin = getExistingAdmin(adminId);

    Image image = Optional.ofNullable(createAnnouncementRequest.getImageUrl())
        .map(Image::new).orElse(null);

    Announcement announcement = new Announcement(createAnnouncementRequest.getTitle(),
        createAnnouncementRequest.getContent(), image, admin);

    announcementJpaRepository.save(announcement);
  }

  public void updateAnnouncement(UUID announcementId,
      UpdateAnnouncementRequest updateAnnouncementRequest, UUID adminId) {

    Announcement announcement = getExistingAnnouncement(announcementId);

    validateAdminExistence(adminId);

    // title 은 null 을 허용하지 않는다.
    updateAnnouncementRequest.getTitle()
        .doIfPresentAndNotNull(announcement::updateTitle);

    // content 는 null 을 허용하지 않는다.
    updateAnnouncementRequest.getContent()
        .doIfPresentAndNotNull(announcement::updateContent);

    // imageUrl 은 null 을 허용한다.
    updateAnnouncementRequest.getImageUrl().doIfPresent(imageUrl -> {
      Image image = Optional.ofNullable(imageUrl).map(Image::new).orElse(null);
      announcement.updateImage(image);
    });

    announcementJpaRepository.save(announcement);
  }

  public void deleteAnnouncement(UUID announcementId, UUID adminId) {

    Announcement announcement = getExistingAnnouncement(announcementId);
    validateAdminExistence(adminId);
    announcementJpaRepository.delete(announcement);
  }

  private void validateAdminExistence(UUID adminId) {
    if (!adminJpaRepository.existsById(adminId)) {
      throw new NotFoundException(ADMIN_NOT_FOUND_ERROR);
    }
  }

  private Admin getExistingAdmin(UUID adminId) {
    return adminJpaRepository.findById(adminId)
        .orElseThrow(() -> new NotFoundException(ADMIN_NOT_FOUND_ERROR));
  }

  private Announcement getExistingAnnouncement(UUID announcementId) {
    return announcementJpaRepository.findById(announcementId)
        .orElseThrow(() -> new NotFoundException(ANNOUNCEMENT_NOT_FOUND_ERROR));
  }
}
