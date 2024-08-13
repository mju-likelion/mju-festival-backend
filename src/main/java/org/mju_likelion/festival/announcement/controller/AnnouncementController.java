package org.mju_likelion.festival.announcement.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.announcement.dto.request.CreateAnnouncementRequest;
import org.mju_likelion.festival.announcement.dto.request.UpdateAnnouncementRequest;
import org.mju_likelion.festival.announcement.dto.response.AnnouncementDetailResponse;
import org.mju_likelion.festival.announcement.dto.response.SimpleAnnouncementsResponse;
import org.mju_likelion.festival.announcement.service.AnnouncementQueryService;
import org.mju_likelion.festival.announcement.service.AnnouncementService;
import org.mju_likelion.festival.common.annotaion.page_number.PageNumber;
import org.mju_likelion.festival.common.annotaion.page_size.PageSize;
import org.mju_likelion.festival.common.authentication.AuthenticationPrincipal;
import org.mju_likelion.festival.common.enums.SortOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/announcements")
public class AnnouncementController {

  private final AnnouncementQueryService announcementQueryService;
  private final AnnouncementService qnnouncementService;

  @GetMapping
  public ResponseEntity<SimpleAnnouncementsResponse> getAnnouncements(
      @RequestParam String sort,
      @RequestParam @PageNumber int page,
      @RequestParam @PageSize int size) {

    return ResponseEntity.ok(
        announcementQueryService.getAnnouncements(SortOrder.fromString(sort), page, size));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AnnouncementDetailResponse> getAnnouncement(@PathVariable UUID id) {

    return ResponseEntity.ok(announcementQueryService.getAnnouncement(id));
  }

  @PostMapping
  public ResponseEntity<Void> createAnnouncement(
      @RequestBody @Valid CreateAnnouncementRequest createAnnouncementRequest,
      @AuthenticationPrincipal UUID adminId) {

    qnnouncementService.createAnnouncement(createAnnouncementRequest, adminId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Void> updateAnnouncement(
      @PathVariable UUID id,
      @RequestBody @Valid UpdateAnnouncementRequest updateAnnouncementRequest,
      @AuthenticationPrincipal UUID adminId) {

    qnnouncementService.updateAnnouncement(id, updateAnnouncementRequest, adminId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAnnouncement(@PathVariable UUID id,
      @AuthenticationPrincipal UUID adminId) {

    qnnouncementService.deleteAnnouncement(id, adminId);
    return ResponseEntity.noContent().build();
  }
}
