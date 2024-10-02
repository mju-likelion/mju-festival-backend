package org.mju_likelion.festival.lost_item.controller;

import static org.mju_likelion.festival.common.api.ApiPaths.DELETE_LOST_ITEM;
import static org.mju_likelion.festival.common.api.ApiPaths.FOUND_LOST_ITEM;
import static org.mju_likelion.festival.common.api.ApiPaths.GET_ALL_LOST_ITEMS;
import static org.mju_likelion.festival.common.api.ApiPaths.GET_LOST_ITEM;
import static org.mju_likelion.festival.common.api.ApiPaths.PATCH_LOST_ITEM;
import static org.mju_likelion.festival.common.api.ApiPaths.POST_LOST_ITEM;
import static org.mju_likelion.festival.common.api.ApiPaths.SEARCH_LOST_ITEMS;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.common.annotaion.page_number.PageNumber;
import org.mju_likelion.festival.common.annotaion.page_size.PageSize;
import org.mju_likelion.festival.common.authentication.AuthenticationPrincipal;
import org.mju_likelion.festival.common.enums.SortOrder;
import org.mju_likelion.festival.lost_item.dto.request.CreateLostItemRequest;
import org.mju_likelion.festival.lost_item.dto.request.LostItemFoundRequest;
import org.mju_likelion.festival.lost_item.dto.request.UpdateLostItemRequest;
import org.mju_likelion.festival.lost_item.dto.response.LostItemDetailResponse;
import org.mju_likelion.festival.lost_item.dto.response.SimpleLostItemsResponse;
import org.mju_likelion.festival.lost_item.service.LostItemQueryService;
import org.mju_likelion.festival.lost_item.service.LostItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class LostItemController {

  private final LostItemQueryService lostItemQueryService;
  private final LostItemService lostItemService;

  @GetMapping(GET_ALL_LOST_ITEMS)
  public ResponseEntity<SimpleLostItemsResponse> getLostItems(
      @RequestParam final String sort,
      @RequestParam @PageNumber final int page,
      @RequestParam @PageSize final int size) {

    return ResponseEntity.ok(
        lostItemQueryService.getLostItems(SortOrder.fromString(sort), page, size));
  }

  @GetMapping(SEARCH_LOST_ITEMS)
  public ResponseEntity<SimpleLostItemsResponse> getLostItems(
      @RequestParam final String sort,
      @RequestParam final String keyword,
      @RequestParam @PageNumber final int page,
      @RequestParam @PageSize final int size) {

    return ResponseEntity.ok(
        lostItemQueryService.searchLostItems(SortOrder.fromString(sort), keyword, page, size));
  }

  @GetMapping(GET_LOST_ITEM)
  public ResponseEntity<LostItemDetailResponse> getLostItem(
      @PathVariable final UUID id) {

    return ResponseEntity.ok(lostItemQueryService.getLostItem(id));
  }

  @PostMapping(POST_LOST_ITEM)
  public ResponseEntity<Void> createLostItem(
      @RequestBody @Valid final CreateLostItemRequest createLostItemRequest,
      @AuthenticationPrincipal final UUID studentCouncilId) {

    lostItemService.createLostItem(createLostItemRequest, studentCouncilId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping(PATCH_LOST_ITEM)
  public ResponseEntity<Void> updateLostItem(
      @PathVariable final UUID id,
      @RequestBody @Valid final UpdateLostItemRequest updateLostItemRequest,
      @AuthenticationPrincipal final UUID studentCouncilId) {

    lostItemService.updateLostItem(id, updateLostItemRequest, studentCouncilId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping(FOUND_LOST_ITEM)
  public ResponseEntity<Void> foundLostItem(
      @PathVariable final UUID id,
      @RequestBody @Valid final LostItemFoundRequest lostItemFoundRequest,
      @AuthenticationPrincipal final UUID studentCouncilId) {

    lostItemService.foundLostItem(id, lostItemFoundRequest, studentCouncilId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(DELETE_LOST_ITEM)
  public ResponseEntity<Void> deleteLostItem(
      @PathVariable final UUID id,
      @AuthenticationPrincipal final UUID studentCouncilId) {

    lostItemService.deleteLostItem(id, studentCouncilId);
    return ResponseEntity.noContent().build();
  }
}
