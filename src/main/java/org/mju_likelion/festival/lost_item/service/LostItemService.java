package org.mju_likelion.festival.lost_item.service;

import static org.mju_likelion.festival.common.config.Resilience4jConfig.REDIS_CIRCUIT_BREAKER;
import static org.mju_likelion.festival.common.util.null_handler.NullHandler.doIfNotNull;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.admin.service.AdminQueryService;
import org.mju_likelion.festival.common.circuit_breaker.FallBackUtil;
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

  @CacheEvict(value = "lostItem", key = "#lostItemId", beforeInvocation = true)
  @CircuitBreaker(name = REDIS_CIRCUIT_BREAKER, fallbackMethod = "updateLostItemFallback")
  public void updateLostItem(
      final UUID lostItemId,
      final UpdateLostItemRequest updateLostItemRequest,
      final UUID studentCouncilId) {

    updateLostItemLogic(lostItemId, updateLostItemRequest, studentCouncilId);
  }

  public void updateLostItemFallback(
      final UUID lostItemId,
      final UpdateLostItemRequest updateLostItemRequest,
      final UUID studentCouncilId,
      final Exception e) {

    FallBackUtil.handleFallBack(e);
    updateLostItemLogic(lostItemId, updateLostItemRequest, studentCouncilId);
  }

  private void updateLostItemLogic(
      final UUID lostItemId,
      final UpdateLostItemRequest updateLostItemRequest,
      final UUID studentCouncilId) {

    LostItem lostItem = lostItemQueryService.getExistLostItem(lostItemId);
    adminQueryService.validateAdminExistence(studentCouncilId);

    updateLostItemFields(lostItem, updateLostItemRequest);

    lostItemJpaRepository.save(lostItem);
  }

  @CacheEvict(value = "lostItem", key = "#lostItemId", beforeInvocation = true)
  @CircuitBreaker(name = REDIS_CIRCUIT_BREAKER, fallbackMethod = "foundLostItemFallback")
  public void foundLostItem(
      final UUID lostItemId,
      final LostItemFoundRequest lostItemFoundRequest,
      final UUID studentCouncilId) {

    foundLostItemLogic(lostItemId, lostItemFoundRequest, studentCouncilId);
  }

  public void foundLostItemFallback(
      final UUID lostItemId,
      final LostItemFoundRequest lostItemFoundRequest,
      final UUID studentCouncilId,
      final Exception e) {

    FallBackUtil.handleFallBack(e);
    foundLostItemLogic(lostItemId, lostItemFoundRequest, studentCouncilId);
  }

  public void foundLostItemLogic(
      final UUID lostItemId,
      final LostItemFoundRequest lostItemFoundRequest,
      final UUID studentCouncilId) {

    LostItem lostItem = lostItemQueryService.getExistLostItem(lostItemId);
    adminQueryService.validateAdminExistence(studentCouncilId);

    lostItem.found(lostItemFoundRequest.getRetrieverInfo());

    lostItemJpaRepository.save(lostItem);
  }

  @CacheEvict(value = "lostItem", key = "#lostItemId", beforeInvocation = true)
  @CircuitBreaker(name = REDIS_CIRCUIT_BREAKER, fallbackMethod = "deleteLostItemFallback")
  public void deleteLostItem(final UUID lostItemId, final UUID studentCouncilId) {
    deleteLostItemLogic(lostItemId, studentCouncilId);
  }

  public void deleteLostItemFallback(
      final UUID lostItemId,
      final UUID studentCouncilId,
      final Exception e) {

    FallBackUtil.handleFallBack(e);
    deleteLostItemLogic(lostItemId, studentCouncilId);
  }

  private void deleteLostItemLogic(final UUID lostItemId, final UUID studentCouncilId) {
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
