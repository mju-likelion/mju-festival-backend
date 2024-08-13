package org.mju_likelion.festival.lost_item.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.ADMIN_NOT_FOUND_ERROR;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.domain.repository.AdminJpaRepository;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.mju_likelion.festival.image.domain.Image;
import org.mju_likelion.festival.lost_item.domain.LostItem;
import org.mju_likelion.festival.lost_item.domain.repository.LostItemJpaRepository;
import org.mju_likelion.festival.lost_item.dto.request.CreateLostItemRequest;
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

  private Admin getExistAdmin(UUID studentCouncilId) {
    return adminJpaRepository.findById(studentCouncilId)
        .orElseThrow(() -> new NotFoundException(ADMIN_NOT_FOUND_ERROR));
  }
}
