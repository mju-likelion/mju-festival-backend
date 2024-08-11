package org.mju_likelion.festival.announcement.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.ADMIN_NOT_FOUND_ERROR;

import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.announcement.domain.Announcement;
import org.mju_likelion.festival.announcement.domain.repository.AnnouncementJpaRepository;
import org.mju_likelion.festival.announcement.dto.request.CreateAnnouncementRequest;
import org.mju_likelion.festival.booth.domain.repository.BoothJpaRepository;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.mju_likelion.festival.image.domain.Image;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnnouncementService {

  private final AnnouncementJpaRepository announcementJpaRepository;
  private final AdminJpaRepository adminJpaRepository;
  private final BoothJpaRepository boothJpaRepository;

  public void createAnnouncement(CreateAnnouncementRequest createAnnouncementRequest,
      UUID adminId) {

    Admin admin = getExistingAdmin(adminId);

    Image image = Optional.ofNullable(createAnnouncementRequest.getImageUrl())
        .map(Image::new)
        .orElse(null);

    Announcement announcement = new Announcement(
        createAnnouncementRequest.getTitle(),
        createAnnouncementRequest.getContent(),
        image,
        admin
    );

    announcementJpaRepository.save(announcement);
  }

  public Admin getExistingAdmin(UUID adminId) {
    return adminJpaRepository.findById(adminId)
        .orElseThrow(() -> new NotFoundException(ADMIN_NOT_FOUND_ERROR));
  }
}
