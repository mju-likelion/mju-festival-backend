package org.mju_likelion.festival.lost_item.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.common.annotaion.page_number.PageNumber;
import org.mju_likelion.festival.common.annotaion.page_size.PageSize;
import org.mju_likelion.festival.common.authentication.AuthenticationPrincipal;
import org.mju_likelion.festival.common.enums.SortOrder;
import org.mju_likelion.festival.lost_item.dto.request.CreateLostItemRequest;
import org.mju_likelion.festival.lost_item.dto.request.UpdateLostItemRequest;
import org.mju_likelion.festival.lost_item.dto.response.SimpleLostItemsResponse;
import org.mju_likelion.festival.lost_item.service.LostItemQueryService;
import org.mju_likelion.festival.lost_item.service.LostItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/lost-items")
public class LostItemController {

  private final LostItemQueryService lostItemQueryService;
  private final LostItemService lostItemService;

  @GetMapping
  public ResponseEntity<SimpleLostItemsResponse> getLostItems(
      @RequestParam String sort,
      @RequestParam @PageNumber int page,
      @RequestParam @PageSize int size) {

    return ResponseEntity.ok(
        lostItemQueryService.getLostItems(SortOrder.fromString(sort), page, size));
  }

  @GetMapping("/search")
  public ResponseEntity<SimpleLostItemsResponse> getLostItems(
      @RequestParam String sort,
      @RequestParam String keyword,
      @RequestParam @PageNumber int page,
      @RequestParam @PageSize int size) {

    return ResponseEntity.ok(
        lostItemQueryService.searchLostItems(SortOrder.fromString(sort), keyword, page, size));
  }

  @PostMapping
  public ResponseEntity<Void> createLostItem(
      @RequestBody @Valid CreateLostItemRequest createLostItemRequest,
      @AuthenticationPrincipal UUID studentCouncilId) {

    lostItemService.createLostItem(createLostItemRequest, studentCouncilId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Void> updateLostItem(
      @PathVariable UUID id,
      @RequestBody @Valid UpdateLostItemRequest updateLostItemRequest,
      @AuthenticationPrincipal UUID studentCouncilId) {

    lostItemService.updateLostItem(id, updateLostItemRequest, studentCouncilId);
    return ResponseEntity.noContent().build();
  }
}
