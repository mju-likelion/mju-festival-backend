package org.mju_likelion.festival.lost_item.service;

import static org.mju_likelion.festival.common.util.null_handler.NullHandler.doIfNotNull;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.service.AdminQueryService;
import org.mju_likelion.festival.image.domain.Image;
import org.mju_likelion.festival.lost_item.domain.LostItem;
import org.mju_likelion.festival.lost_item.domain.repository.LostItemJpaRepository;
import org.mju_likelion.festival.lost_item.dto.request.CreateLostItemRequest;
import org.mju_likelion.festival.lost_item.dto.request.LostItemFoundRequest;
import org.mju_likelion.festival.lost_item.dto.request.UpdateLostItemRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LostItemService {

  private final AdminQueryService adminQueryService;
  private final LostItemQueryService lostItemQueryService;
  private final LostItemJpaRepository lostItemJpaRepository;

  public void createLostItem(
      final CreateLostItemRequest createLostItemRequest,
      final UUID studentCouncilId) {

    Admin admin = adminQueryService.getExistingAdmin(studentCouncilId);
    Image image = new Image(createLostItemRequest.getImageUrl());

    LostItem lostItem = new LostItem(
        createLostItemRequest.getTitle(),
        createLostItemRequest.getContent(),
        image,
        admin);

    lostItemJpaRepository.save(lostItem);
  }

  @CacheEvict(value = "lostItem", key = "#lostItemId")
  public void updateLostItem(
      final UUID lostItemId,
      final UpdateLostItemRequest updateLostItemRequest,
      final UUID studentCouncilId) {

    LostItem lostItem = lostItemQueryService.getExistLostItem(lostItemId);
    adminQueryService.validateAdminExistence(studentCouncilId);

    updateLostItemFields(lostItem, updateLostItemRequest);

    lostItemJpaRepository.save(lostItem);
  }

  @CacheEvict(value = "lostItem", key = "#lostItemId")
  public void foundLostItem(
      final UUID lostItemId,
      final LostItemFoundRequest lostItemFoundRequest,
      final UUID studentCouncilId) {

    LostItem lostItem = lostItemQueryService.getExistLostItem(lostItemId);
    adminQueryService.validateAdminExistence(studentCouncilId);

    lostItem.found(lostItemFoundRequest.getRetrieverInfo());

    lostItemJpaRepository.save(lostItem);
  }

  @CacheEvict(value = "lostItem", key = "#lostItemId")
  public void deleteLostItem(final UUID lostItemId, final UUID studentCouncilId) {
    LostItem lostItem = lostItemQueryService.getExistLostItem(lostItemId);
    adminQueryService.validateAdminExistence(studentCouncilId);

    lostItemJpaRepository.delete(lostItem);
  }

  private void updateLostItemFields(final LostItem lostItem,
      final UpdateLostItemRequest updateLostItemRequest) {
    doIfNotNull(updateLostItemRequest.getTitle(), lostItem::updateTitle);
    doIfNotNull(updateLostItemRequest.getContent(), lostItem::updateContent);
    doIfNotNull(updateLostItemRequest.getImageUrl(), url -> lostItem.updateImage(new Image(url)));
  }
}
