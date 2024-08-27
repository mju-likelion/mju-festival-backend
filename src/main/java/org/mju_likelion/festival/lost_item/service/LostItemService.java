package org.mju_likelion.festival.lost_item.service;

import static org.mju_likelion.festival.common.util.null_handler.NullHandler.doIfNotNull;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.image.domain.Image;
import org.mju_likelion.festival.lost_item.domain.LostItem;
import org.mju_likelion.festival.lost_item.domain.repository.LostItemJpaRepository;
import org.mju_likelion.festival.lost_item.dto.request.CreateLostItemRequest;
import org.mju_likelion.festival.lost_item.dto.request.LostItemFoundRequest;
import org.mju_likelion.festival.lost_item.dto.request.UpdateLostItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LostItemService {

  private final LostItemJpaRepository lostItemJpaRepository;
  private final LostItemServiceUtil lostItemServiceUtil;

  public void createLostItem(
      final CreateLostItemRequest createLostItemRequest,
      final UUID studentCouncilId) {

    Admin admin = lostItemServiceUtil.getExistAdmin(studentCouncilId);
    Image image = new Image(createLostItemRequest.getImageUrl());

    LostItem lostItem = new LostItem(
        createLostItemRequest.getTitle(),
        createLostItemRequest.getContent(),
        image,
        admin);

    lostItemJpaRepository.save(lostItem);
  }

  public void updateLostItem(
      final UUID lostItemId,
      final UpdateLostItemRequest updateLostItemRequest,
      final UUID studentCouncilId) {

    LostItem lostItem = lostItemServiceUtil.getExistLostItem(lostItemId);

    lostItemServiceUtil.validateAdminExistence(studentCouncilId);

    updateLostItemFields(lostItem, updateLostItemRequest);

    lostItemJpaRepository.save(lostItem);
  }

  public void foundLostItem(
      final UUID lostItemId,
      final LostItemFoundRequest lostItemFoundRequest,
      final UUID studentCouncilId) {

    LostItem lostItem = lostItemServiceUtil.getExistLostItem(lostItemId);
    lostItemServiceUtil.validateAdminExistence(studentCouncilId);

    lostItem.found(lostItemFoundRequest.getRetrieverInfo());

    lostItemJpaRepository.save(lostItem);
  }

  public void deleteLostItem(final UUID lostItemId, final UUID studentCouncilId) {
    LostItem lostItem = lostItemServiceUtil.getExistLostItem(lostItemId);
    lostItemServiceUtil.validateAdminExistence(studentCouncilId);

    lostItemJpaRepository.delete(lostItem);
  }

  private void updateLostItemFields(final LostItem lostItem,
      final UpdateLostItemRequest updateLostItemRequest) {
    doIfNotNull(updateLostItemRequest.getTitle(), lostItem::updateTitle);
    doIfNotNull(updateLostItemRequest.getContent(), lostItem::updateContent);
    doIfNotNull(updateLostItemRequest.getImageUrl(), url -> lostItem.updateImage(new Image(url)));
  }
}
