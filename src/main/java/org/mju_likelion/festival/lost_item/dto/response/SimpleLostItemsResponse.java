package org.mju_likelion.festival.lost_item.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.lost_item.domain.SimpleLostItem;

/**
 * 분실물 간단 정보 응답 DTO.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleLostItemsResponse {

  private final List<SimpleLostItem> simpleLostItems;
  private final int totalPage;

  public static SimpleLostItemsResponse of(
      final List<SimpleLostItem> simpleLostItem,
      final int totalPage) {

    return new SimpleLostItemsResponse(simpleLostItem, totalPage);
  }

  @Override
  public String toString() {
    return "SimpleLostItemsResponse{" +
        "simpleLostItems=" + simpleLostItems +
        ", totalPage=" + totalPage +
        '}';
  }
}
