package org.mju_likelion.festival.booth.controller;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.booth.dto.response.BoothDetailResponse;
import org.mju_likelion.festival.booth.dto.response.BoothQrResponse;
import org.mju_likelion.festival.booth.dto.response.SimpleBoothResponse;
import org.mju_likelion.festival.booth.service.BoothQueryService;
import org.mju_likelion.festival.common.authentication.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/booths")
public class BoothController {

  private final BoothQueryService boothQueryService;

  @GetMapping
  public ResponseEntity<List<SimpleBoothResponse>> getBooths(
      @RequestParam final int page,
      @RequestParam final int size) {
    return ResponseEntity.ok(boothQueryService.getBooths(page, size));
  }

  @GetMapping("/{id}")
  public ResponseEntity<BoothDetailResponse> getBooth(@PathVariable final UUID id) {
    return ResponseEntity.ok(boothQueryService.getBooth(id));
  }

  @GetMapping("/{id}/qr")
  public ResponseEntity<BoothQrResponse> getBoothQr(@PathVariable final UUID id,
      @AuthenticationPrincipal final UUID boothAdminId) {
    return ResponseEntity.ok(boothQueryService.getBoothQr(id, boothAdminId));
  }
}
