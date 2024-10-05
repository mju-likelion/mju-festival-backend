package org.mju_likelion.festival.announcement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.AdminRole;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.announcement.domain.Announcement;
import org.mju_likelion.festival.announcement.domain.repository.AnnouncementJpaRepository;
import org.mju_likelion.festival.announcement.dto.request.CreateAnnouncementRequest;
import org.mju_likelion.festival.announcement.dto.request.UpdateAnnouncementRequest;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.mju_likelion.festival.common.util.field_wrapper.FieldWrapper;
import org.mju_likelion.festival.image.domain.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("AnnouncementService")
@ApplicationTest
@Transactional
public class AnnouncementServiceTest {

  private static Admin studentCouncilAdmin;
  @Autowired
  private AnnouncementService announcementService;
  @Autowired
  private AnnouncementJpaRepository announcementJpaRepository;
  @Autowired
  private AdminJpaRepository adminJpaRepository;

  @BeforeEach
  void setUp() {
    studentCouncilAdmin = adminJpaRepository.findByRole(AdminRole.STUDENT_COUNCIL)
        .orElseThrow();
  }

  @AfterEach
  void tearDown() {
    announcementJpaRepository.deleteAll(
        announcementJpaRepository.findByWriterId(studentCouncilAdmin.getId()));
  }

  @Test
  @DisplayName("공지사항을 생성한다.")
  void createAnnouncement() {
    // given
    CreateAnnouncementRequest request = createRequest("공지사항 제목", "공지사항 내용", "이미지 URL");

    // when
    announcementService.createAnnouncement(request, studentCouncilAdmin.getId());

    // then
    Announcement announcement = announcementJpaRepository.findByWriterId(
        studentCouncilAdmin.getId()).get(0);

    assertThat(announcement.getWriter().getId()).isEqualTo(studentCouncilAdmin.getId());
  }

  @Test
  @DisplayName("공지사항을 이미지를 포함해 수정한다.")
  void updateAnnouncement() {
    // given
    Announcement announcement = createAndSaveAnnouncement("공지사항 제목", "공지사항 내용", "이미지 URL");
    UpdateAnnouncementRequest request = new UpdateAnnouncementRequest();
    request.setTitle("수정된 공지사항 제목");
    request.setContent("수정된 공지사항 내용");
    request.setImageUrl(new FieldWrapper<>(true, "수정된 이미지 URL"));

    // when
    announcementService.updateAnnouncement(announcement.getId(), request,
        studentCouncilAdmin.getId());

    // then
    Announcement updatedAnnouncement = announcementJpaRepository.findById(announcement.getId())
        .orElseThrow();
    assertUpdatedAnnouncement(updatedAnnouncement, "수정된 공지사항 제목", "수정된 공지사항 내용", "수정된 이미지 URL");
  }

  @Test
  @DisplayName("공지사항을 이미지를 포함하지 않고 수정한다.")
  void updateAnnouncementWithNullImageUrl() {
    // given
    Announcement announcement = createAndSaveAnnouncement("공지사항 제목", "공지사항 내용", "이미지 URL");
    UpdateAnnouncementRequest request = new UpdateAnnouncementRequest();
    request.setTitle("수정된 공지사항 제목");
    request.setContent("수정된 공지사항 내용");
    request.setImageUrl(new FieldWrapper<>(false, null));

    // when
    announcementService.updateAnnouncement(announcement.getId(), request,
        studentCouncilAdmin.getId());

    // then
    Announcement updatedAnnouncement = announcementJpaRepository.findById(announcement.getId())
        .orElseThrow();
    assertUpdatedAnnouncement(updatedAnnouncement, "수정된 공지사항 제목", "수정된 공지사항 내용", "이미지 URL");
  }

  @Test
  @DisplayName("공지사항을 이미지를 null로 수정한다.")
  void updateAnnouncementWithNullImage() {
    // given
    Announcement announcement = createAndSaveAnnouncement("공지사항 제목", "공지사항 내용", "이미지 URL");
    UpdateAnnouncementRequest request = new UpdateAnnouncementRequest();
    request.setTitle("수정된 공지사항 제목");
    request.setContent("수정된 공지사항 내용");
    request.setImageUrl(new FieldWrapper<>(true, null));

    // when
    announcementService.updateAnnouncement(announcement.getId(), request,
        studentCouncilAdmin.getId());

    // then
    Announcement updatedAnnouncement = announcementJpaRepository.findById(announcement.getId())
        .orElseThrow();
    assertUpdatedAnnouncement(updatedAnnouncement, "수정된 공지사항 제목", "수정된 공지사항 내용", null);
  }

  @Test
  @DisplayName("공지사항을 삭제한다.")
  void deleteAnnouncement() {
    // given
    Announcement announcement = new Announcement("공지사항 제목", "공지사항 내용", null, studentCouncilAdmin);
    announcementJpaRepository.saveAndFlush(announcement);

    // when
    announcementService.deleteAnnouncement(announcement.getId(), studentCouncilAdmin.getId());

    // then
    assertThat(announcementJpaRepository.findById(announcement.getId())).isEmpty();
  }

  private CreateAnnouncementRequest createRequest(String title, String content, String imageUrl) {
    return new CreateAnnouncementRequest(title, content, imageUrl);
  }

  private Announcement createAndSaveAnnouncement(String title, String content, String imageUrl) {
    Image image = new Image(imageUrl);
    Announcement announcement = new Announcement(title, content, image, studentCouncilAdmin);
    announcementJpaRepository.saveAndFlush(announcement);
    return announcement;
  }

  private void assertUpdatedAnnouncement(Announcement announcement, String expectedTitle,
      String expectedContent, String expectedImageUrl) {
    assertAll(
        () -> assertThat(announcement.getTitle()).isEqualTo(expectedTitle),
        () -> assertThat(announcement.getContent()).isEqualTo(expectedContent),
        () -> assertThat(
            announcement.getImage() != null ? announcement.getImage().getUrl() : null).isEqualTo(
            expectedImageUrl)
    );
  }
}
