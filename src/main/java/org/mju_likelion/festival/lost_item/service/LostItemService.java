package org.mju_likelion.festival.lost_item.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.ADMIN_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.LOST_ITEM_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.util.null_handler.NullHandler.doIfNotNull;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.mju_likelion.festival.image.domain.Image;
import org.mju_likelion.festival.lost_item.domain.LostItem;
import org.mju_likelion.festival.lost_item.domain.repository.LostItemJpaRepository;
import org.mju_likelion.festival.lost_item.dto.request.CreateLostItemRequest;
import org.mju_likelion.festival.lost_item.dto.request.LostItemFoundRequest;
import org.mju_likelion.festival.lost_item.dto.request.UpdateLostItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class LostItemService {

  private final LostItemJpaRepository lostItemJpaRepository;
  private final AdminJpaRepository adminJpaRepository;

  public void createLostItem(CreateLostItemRequest createLostItemRequest, UUID studentCouncilId) {

    Admin admin = getExistAdmin(studentCouncilId);
    Image image = new Image(createLostItemRequest.getImageUrl());

    LostItem lostItem = new LostItem(
        createLostItemRequest.getTitle(),
        createLostItemRequest.getContent(),
        image,
        admin);

    lostItemJpaRepository.save(lostItem);
  }

  public void updateLostItem(UUID lostItemId, UpdateLostItemRequest updateLostItemRequest,
      UUID studentCouncilId) {
    LostItem lostItem = getExistLostItem(lostItemId);

    validateAdminExistence(studentCouncilId);

    doIfNotNull(updateLostItemRequest.getTitle(), lostItem::updateTitle);
    doIfNotNull(updateLostItemRequest.getContent(), lostItem::updateContent);
    doIfNotNull(updateLostItemRequest.getImageUrl(), url -> lostItem.updateImage(new Image(url)));

    lostItemJpaRepository.save(lostItem);
  }

  public void foundLostItem(UUID lostItemId, LostItemFoundRequest lostItemFoundRequest,
      UUID studentCouncilId) {
    LostItem lostItem = getExistLostItem(lostItemId);
    validateAdminExistence(studentCouncilId);

    lostItem.found(lostItemFoundRequest.getRetrieverInfo());

    lostItemJpaRepository.save(lostItem);
  }

  private void validateAdminExistence(UUID adminId) {
    if (!adminJpaRepository.existsById(adminId)) {
      throw new NotFoundException(ADMIN_NOT_FOUND_ERROR);
    }
  }

  private LostItem getExistLostItem(UUID lostItemId) {
    return lostItemJpaRepository.findById(lostItemId)
        .orElseThrow(() -> new NotFoundException(LOST_ITEM_NOT_FOUND_ERROR));
  }

  private Admin getExistAdmin(UUID studentCouncilId) {
    return adminJpaRepository.findById(studentCouncilId)
        .orElseThrow(() -> new NotFoundException(ADMIN_NOT_FOUND_ERROR));
  }
}
