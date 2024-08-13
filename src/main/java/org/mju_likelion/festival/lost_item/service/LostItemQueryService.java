package org.mju_likelion.festival.lost_item.service;

import static org.mju_likelion.festival.common.exception.type.ErrorType.PAGE_OUT_OF_BOUND_ERROR;

import java.util.List;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.common.enums.SortOrder;
import org.mju_likelion.festival.common.exception.NotFoundException;
import org.mju_likelion.festival.lost_item.domain.SimpleLostItem;
import org.mju_likelion.festival.lost_item.domain.repository.LostItemQueryRepository;
import org.mju_likelion.festival.lost_item.dto.response.SimpleLostItemsResponse;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LostItemQueryService {

  private final LostItemQueryRepository lostItemQueryRepository;


  public SimpleLostItemsResponse getLostItems(final SortOrder sort, final int page,
      final int size) {

    int totalPage = lostItemQueryRepository.findTotalPage(size);

    validatePage(page, totalPage);

    List<SimpleLostItem> simpleLostItems = lostItemQueryRepository.findOrderedSimpleLostItemsWithPagenation(
        sort, page,
        size);

    return SimpleLostItemsResponse.of(simpleLostItems, totalPage);
  }

  private void validatePage(int page, int totalPage) {
    if (page >= totalPage) {
      throw new NotFoundException(PAGE_OUT_OF_BOUND_ERROR);
    }
  }
}