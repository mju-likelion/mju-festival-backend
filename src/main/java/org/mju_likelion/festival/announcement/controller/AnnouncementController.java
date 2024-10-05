package org.mju_likelion.festival.announcement.controller;

import static org.mju_likelion.festival.common.api.ApiPaths.DELETE_ANNOUNCEMENT;
import static org.mju_likelion.festival.common.api.ApiPaths.GET_ALL_ANNOUNCEMENTS;
import static org.mju_likelion.festival.common.api.ApiPaths.GET_ANNOUNCEMENT;
import static org.mju_likelion.festival.common.api.ApiPaths.PATCH_ANNOUNCEMENT;
import static org.mju_likelion.festival.common.api.ApiPaths.POST_ANNOUNCEMENT;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AnnouncementController {

  private final AnnouncementQueryService announcementQueryService;
  private final AnnouncementService announcementService;

  @GetMapping(GET_ALL_ANNOUNCEMENTS)
  public ResponseEntity<SimpleAnnouncementsResponse> getAnnouncements(
      @RequestParam final String sort,
      @RequestParam @PageNumber final int page,
      @RequestParam @PageSize final int size) {

    return ResponseEntity.ok(
        announcementQueryService.getAnnouncements(SortOrder.fromString(sort), page, size));
  }

  @GetMapping(GET_ANNOUNCEMENT)
  public ResponseEntity<AnnouncementDetailResponse> getAnnouncement(
      @PathVariable final UUID id) {

    return ResponseEntity.ok(announcementQueryService.getAnnouncement(id));
  }

  @PostMapping(POST_ANNOUNCEMENT)
  public ResponseEntity<Void> createAnnouncement(
      @RequestBody @Valid final CreateAnnouncementRequest createAnnouncementRequest,
      @AuthenticationPrincipal final UUID adminId) {

    announcementService.createAnnouncement(createAnnouncementRequest, adminId);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping(PATCH_ANNOUNCEMENT)
  public ResponseEntity<Void> updateAnnouncement(
      @PathVariable final UUID id,
      @RequestBody @Valid final UpdateAnnouncementRequest updateAnnouncementRequest,
      @AuthenticationPrincipal final UUID adminId) {

    announcementService.updateAnnouncement(id, updateAnnouncementRequest, adminId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping(DELETE_ANNOUNCEMENT)
  public ResponseEntity<Void> deleteAnnouncement(
      @PathVariable final UUID id,
      @AuthenticationPrincipal final UUID adminId) {

    announcementService.deleteAnnouncement(id, adminId);
    return ResponseEntity.noContent().build();
  }
}
