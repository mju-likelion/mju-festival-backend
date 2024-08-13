package org.mju_likelion.festival.lost_item.controller;

import lombok.AllArgsConstructor;
import org.mju_likelion.festival.lost_item.dto.response.SimpleLostItemsResponse;
import org.mju_likelion.festival.lost_item.service.LostItemQueryService;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/lost-items")
public class LostItemController {

  private final LostItemQueryService lostItemQueryService;

  @GetMapping
  public ResponseEntity<SimpleLostItemsResponse> getLostItems(@RequestParam String sort,
      @RequestParam int page, @RequestParam int size) {

    return ResponseEntity.ok(
        lostItemQueryService.getLostItems(Direction.fromString(sort), page, size));
  }
}
