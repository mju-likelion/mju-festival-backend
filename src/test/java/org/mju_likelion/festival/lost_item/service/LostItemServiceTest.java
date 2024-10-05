package org.mju_likelion.festival.lost_item.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.AdminRole;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.common.annotation.ApplicationTest;
import org.mju_likelion.festival.common.exception.ConflictException;
import org.mju_likelion.festival.lost_item.domain.LostItem;
import org.mju_likelion.festival.lost_item.domain.repository.LostItemJpaRepository;
import org.mju_likelion.festival.lost_item.dto.request.CreateLostItemRequest;
import org.mju_likelion.festival.lost_item.dto.request.LostItemFoundRequest;
import org.mju_likelion.festival.lost_item.dto.request.UpdateLostItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("LostItemService")
@ApplicationTest
@Transactional
public class LostItemServiceTest {

  private static Admin studentCouncilAdmin;
  @Autowired
  private LostItemService lostItemService;
  @Autowired
  private LostItemJpaRepository lostItemJpaRepository;
  @Autowired
  private AdminJpaRepository adminJpaRepository;

  @BeforeEach
  void setUp() {
    studentCouncilAdmin = adminJpaRepository.findByRole(AdminRole.STUDENT_COUNCIL)
        .orElseThrow();
  }

  @AfterEach
  void tearDown() {
    lostItemJpaRepository.deleteAll(
        lostItemJpaRepository.findByWriterId(studentCouncilAdmin.getId()));
  }

  @DisplayName("분실물을 생성한다.")
  @Test
  void createLostItem() {
    // given
    CreateLostItemRequest request = new CreateLostItemRequest("분실물 제목", "분실물 내용", "이미지 URL");

    // when
    lostItemService.createLostItem(request, studentCouncilAdmin.getId());

    // then
    LostItem lostItem = lostItemJpaRepository.findByWriterId(studentCouncilAdmin.getId()).get(0);
    assertThat(lostItem.getTitle()).isEqualTo(request.getTitle());
    assertThat(lostItem.getContent()).isEqualTo(request.getContent());
    assertThat(lostItem.getImage().getUrl()).isEqualTo(request.getImageUrl());
  }

  @DisplayName("분실물의 제목을 수정한다.")
  @Test
  void updateLostItemTitle() {
    // given
    CreateLostItemRequest request = new CreateLostItemRequest("분실물 제목", "분실물 내용", "이미지 URL");
    lostItemService.createLostItem(request, studentCouncilAdmin.getId());

    LostItem lostItem = lostItemJpaRepository.findByWriterId(studentCouncilAdmin.getId()).get(0);

    String newTitle = "수정된 분실물 제목";
    UpdateLostItemRequest updateRequest = new UpdateLostItemRequest(newTitle, null, null);

    // when
    lostItemService.updateLostItem(lostItem.getId(), updateRequest, studentCouncilAdmin.getId());

    // then
    LostItem updatedLostItem = lostItemJpaRepository.findByWriterId(studentCouncilAdmin.getId())
        .get(0);
    assertThat(updatedLostItem.getTitle()).isEqualTo(newTitle);
  }

  @DisplayName("분실물의 내용을 수정한다.")
  @Test
  void updateLostItemContent() {
    // given
    CreateLostItemRequest request = new CreateLostItemRequest("분실물 제목", "분실물 내용", "이미지 URL");
    lostItemService.createLostItem(request, studentCouncilAdmin.getId());

    LostItem lostItem = lostItemJpaRepository.findByWriterId(studentCouncilAdmin.getId()).get(0);

    String newContent = "수정된 분실물 내용";
    UpdateLostItemRequest updateRequest = new UpdateLostItemRequest(null, newContent, null);

    // when
    lostItemService.updateLostItem(lostItem.getId(), updateRequest, studentCouncilAdmin.getId());

    // then
    LostItem updatedLostItem = lostItemJpaRepository.findByWriterId(studentCouncilAdmin.getId())
        .get(0);
    assertThat(updatedLostItem.getContent()).isEqualTo(newContent);
  }

  @DisplayName("분실물의 이미지 URL을 수정한다.")
  @Test
  void updateLostItemImageUrl() {
    // given
    CreateLostItemRequest request = new CreateLostItemRequest("분실물 제목", "분실물 내용", "이미지 URL");
    lostItemService.createLostItem(request, studentCouncilAdmin.getId());

    LostItem lostItem = lostItemJpaRepository.findByWriterId(studentCouncilAdmin.getId()).get(0);

    String newImageUrl = "수정된 이미지 URL";
    UpdateLostItemRequest updateRequest = new UpdateLostItemRequest(null, null, newImageUrl);

    // when
    lostItemService.updateLostItem(lostItem.getId(), updateRequest, studentCouncilAdmin.getId());

    // then
    LostItem updatedLostItem = lostItemJpaRepository.findByWriterId(studentCouncilAdmin.getId())
        .get(0);
    assertThat(updatedLostItem.getImage().getUrl()).isEqualTo(newImageUrl);
  }

  @DisplayName("분실물을 찾는다.")
  @Test
  void foundLostItem() {
    // given
    CreateLostItemRequest request = new CreateLostItemRequest("분실물 제목", "분실물 내용", "이미지 URL");
    lostItemService.createLostItem(request, studentCouncilAdmin.getId());

    LostItem lostItem = lostItemJpaRepository.findByWriterId(studentCouncilAdmin.getId()).get(0);

    String retrieverInfo = "분실물을 찾은 사람 정보";
    LostItemFoundRequest foundRequest = new LostItemFoundRequest(retrieverInfo);

    // when
    lostItemService.foundLostItem(lostItem.getId(), foundRequest, studentCouncilAdmin.getId());

    // then
    LostItem foundLostItem = lostItemJpaRepository.findByWriterId(studentCouncilAdmin.getId())
        .get(0);
    assertThat(foundLostItem.getRetrieverInfo()).isEqualTo(retrieverInfo);
  }

  @DisplayName("이미 찾은 분실물을 다시 찾을 수 없다.")
  @Test
  void foundLostItemTwice() {
    // given
    CreateLostItemRequest request = new CreateLostItemRequest("분실물 제목", "분실물 내용", "이미지 URL");
    lostItemService.createLostItem(request, studentCouncilAdmin.getId());

    LostItem lostItem = lostItemJpaRepository.findByWriterId(studentCouncilAdmin.getId()).get(0);

    String retrieverInfo = "분실물을 찾은 사람 정보";
    LostItemFoundRequest foundRequest = new LostItemFoundRequest(retrieverInfo);
    lostItemService.foundLostItem(lostItem.getId(), foundRequest, studentCouncilAdmin.getId());

    // when, then
    assertThatThrownBy(() -> lostItemService.foundLostItem(lostItem.getId(), foundRequest,
        studentCouncilAdmin.getId()))
        .isInstanceOf(ConflictException.class);
  }

  @DisplayName("분실물을 삭제한다.")
  @Test
  void deleteLostItem() {
    // given
    CreateLostItemRequest request = new CreateLostItemRequest("분실물 제목", "분실물 내용", "이미지 URL");
    lostItemService.createLostItem(request, studentCouncilAdmin.getId());

    LostItem lostItem = lostItemJpaRepository.findByWriterId(studentCouncilAdmin.getId()).get(0);

    // when
    lostItemService.deleteLostItem(lostItem.getId(), studentCouncilAdmin.getId());

    // then
    assertThat(lostItemJpaRepository.findByWriterId(studentCouncilAdmin.getId())).isEmpty();
  }
}
