package org.mju_likelion.festival.lost_item.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.LOST_ITEM_NOT_FOUND_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.PAGE_OUT_OF_BOUND_ERROR;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.common.enums.SortOrder;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.mju_likelion.festival.lost_item.domain.LostItem;
import org.mju_likelion.festival.lost_item.domain.SimpleLostItem;
import org.mju_likelion.festival.lost_item.domain.repository.LostItemJpaRepository;
import org.mju_likelion.festival.lost_item.domain.repository.LostItemQueryRepository;
import org.mju_likelion.festival.lost_item.dto.response.SimpleLostItemsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LostItemQueryService {

  private final LostItemQueryRepository lostItemQueryRepository;
  private final LostItemJpaRepository lostItemJpaRepository;

  public SimpleLostItemsResponse getLostItems(
      final SortOrder sort,
      final int page,
      final int size) {

    int totalPage = lostItemQueryRepository.findTotalPage(size);

    validatePage(page, totalPage);

    List<SimpleLostItem> simpleLostItems = lostItemQueryRepository.findOrderedSimpleLostItemsWithPagenation(
        sort, page, size);

    return SimpleLostItemsResponse.of(simpleLostItems, totalPage);
  }

  public SimpleLostItemsResponse searchLostItems(
      final SortOrder sort,
      final String keyword,
      final int page,
      final int size) {

    int totalPage = lostItemQueryRepository.findTotalPageByKeyword(keyword, size);

    validatePage(page, totalPage);

    List<SimpleLostItem> simpleLostItems = lostItemQueryRepository
        .findOrderedSimpleLostItemsWithPagenationByKeyword(sort, keyword, page, size);

    return SimpleLostItemsResponse.of(simpleLostItems, totalPage);
  }

  public LostItem getExistLostItem(final UUID lostItemId) {
    return lostItemJpaRepository.findById(lostItemId)
        .orElseThrow(() -> new NotFoundException(LOST_ITEM_NOT_FOUND_ERROR));
  }

  public void validatePage(final int page, final int totalPage) {
    if (page != 0 && page >= totalPage) {
      throw new NotFoundException(PAGE_OUT_OF_BOUND_ERROR);
    }
  }
}
