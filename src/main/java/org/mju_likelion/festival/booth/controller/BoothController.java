package org.mju_likelion.festival.booth.controller;

import static org.mju_likelion.festival.common.api.ApiPaths.GET_ALL_BOOTHS;
import static org.mju_likelion.festival.common.api.ApiPaths.GET_ALL_BOOTH_DEPARTMENTS;
import static org.mju_likelion.festival.common.api.ApiPaths.GET_BOOTH;
import static org.mju_likelion.festival.common.api.ApiPaths.GET_BOOTH_MANAGING_DETAIL;
import static org.mju_likelion.festival.common.api.ApiPaths.ISSUE_BOOTH_QR;
import static org.mju_likelion.festival.common.api.ApiPaths.PATCH_BOOTH;
import static org.mju_likelion.festival.common.api.ApiPaths.VISIT_BOOTH;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.booth.dto.request.UpdateBoothRequest;
import org.mju_likelion.festival.booth.dto.response.BoothDepartmentResponse;
import org.mju_likelion.festival.booth.dto.response.BoothDetailResponse;
import org.mju_likelion.festival.booth.dto.response.BoothManagingDetailResponse;
import org.mju_likelion.festival.booth.dto.response.BoothQrResponse;
import org.mju_likelion.festival.booth.dto.response.SimpleBoothsResponse;
import org.mju_likelion.festival.booth.service.BoothQueryService;
import org.mju_likelion.festival.booth.service.BoothService;
import org.mju_likelion.festival.booth.util.qr.BoothQrStrategy;
import org.mju_likelion.festival.common.authentication.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BoothController {

  private final BoothQueryService boothQueryService;
  private final BoothService boothService;

  @GetMapping(GET_ALL_BOOTH_DEPARTMENTS)
  public ResponseEntity<List<BoothDepartmentResponse>> getBoothDepartments() {

    return ResponseEntity.ok(boothQueryService.getBoothDepartments());
  }

  @GetMapping(GET_ALL_BOOTHS)
  public ResponseEntity<List<SimpleBoothsResponse>> getBooths(
      @RequestParam(name = "department_id") final UUID departmentId) {

    return ResponseEntity.ok(boothQueryService.getBooths(departmentId));
  }

  @GetMapping(GET_BOOTH)
  public ResponseEntity<BoothDetailResponse> getBooth(
      @PathVariable final UUID id) {

    return ResponseEntity.ok(boothQueryService.getBooth(id));
  }

  @GetMapping(GET_BOOTH_MANAGING_DETAIL)
  public ResponseEntity<BoothManagingDetailResponse> getBoothManagingDetail(
      @PathVariable final UUID id,
      @AuthenticationPrincipal final UUID boothAdminId) {

    return ResponseEntity.ok(boothQueryService.getBoothManagingDetail(id, boothAdminId));
  }

  @GetMapping(ISSUE_BOOTH_QR)
  public ResponseEntity<BoothQrResponse> getBoothQr(
      @PathVariable final UUID id,
      @AuthenticationPrincipal final UUID boothAdminId) {

    return ResponseEntity.ok(boothQueryService.getBoothQr(id, boothAdminId));
  }

  @PostMapping(VISIT_BOOTH)
  public ResponseEntity<Void> visitBooth(
      @PathVariable final String qrId,
      @RequestParam final String strategy,
      @AuthenticationPrincipal final UUID userId) {

    boothService.visitBooth(qrId, BoothQrStrategy.fromString(strategy), userId);
    return ResponseEntity.ok().build();
  }

  @PatchMapping(PATCH_BOOTH)
  public ResponseEntity<Void> updateBooth(
      @PathVariable final UUID id,
      @RequestBody final UpdateBoothRequest updateBoothRequest,
      @AuthenticationPrincipal final UUID boothAdminId) {

    boothService.updateBooth(id, updateBoothRequest, boothAdminId);
    return ResponseEntity.noContent().build();
  }
}
