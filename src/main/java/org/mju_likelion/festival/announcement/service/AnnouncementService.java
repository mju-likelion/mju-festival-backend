package org.mju_likelion.festival.announcement.service;

import static org.mju_likelion.festival.common.util.null_handler.NullHandler.doIfNotNull;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.service.AdminQueryService;
import org.mju_likelion.festival.announcement.domain.Announcement;
import org.mju_likelion.festival.announcement.domain.repository.AnnouncementJpaRepository;
import org.mju_likelion.festival.announcement.dto.request.CreateAnnouncementRequest;
import org.mju_likelion.festival.announcement.dto.request.UpdateAnnouncementRequest;
import org.mju_likelion.festival.image.domain.Image;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementService {

  private final AdminQueryService adminQueryService;
  private final AnnouncementQueryService announcementQueryService;
  private final AnnouncementJpaRepository announcementJpaRepository;

  public void createAnnouncement(
      final CreateAnnouncementRequest createAnnouncementRequest,
      final UUID adminId) {

    Admin admin = adminQueryService.getExistingAdmin(adminId);

    Image image = Optional.ofNullable(createAnnouncementRequest.getImageUrl())
        .map(Image::new).orElse(null);

    Announcement announcement = new Announcement(createAnnouncementRequest.getTitle(),
        createAnnouncementRequest.getContent(), image, admin);

    announcementJpaRepository.save(announcement);
  }

  public void updateAnnouncement(
      final UUID announcementId,
      final UpdateAnnouncementRequest updateAnnouncementRequest,
      final UUID adminId) {

    Announcement announcement = announcementQueryService.getExistingAnnouncement(announcementId);

    adminQueryService.validateAdminExistence(adminId);

    doIfNotNull(updateAnnouncementRequest.getTitle(), announcement::updateTitle);
    doIfNotNull(updateAnnouncementRequest.getContent(), announcement::updateContent);

    updateAnnouncementRequest.getImageUrl().doIfPresent(imageUrl -> {
      Image image = Optional.ofNullable(imageUrl).map(Image::new).orElse(null);
      announcement.updateImage(image);
    });

    announcementJpaRepository.save(announcement);
  }

  public void deleteAnnouncement(final UUID announcementId, final UUID adminId) {
    Announcement announcement = announcementQueryService.getExistingAnnouncement(announcementId);
    adminQueryService.validateAdminExistence(adminId);
    announcementJpaRepository.delete(announcement);
  }
}
