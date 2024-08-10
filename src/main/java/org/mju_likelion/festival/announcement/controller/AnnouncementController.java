package org.mju_likelion.festival.announcement.controller;

import lombok.AllArgsConstructor;
import org.mju_likelion.festival.announcement.dto.response.SimpleAnnouncementsResponse;
import org.mju_likelion.festival.announcement.service.AnnouncementQueryService;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/announcements")
public class AnnouncementController {

  private final AnnouncementQueryService announcementQueryService;

  @GetMapping
  public ResponseEntity<SimpleAnnouncementsResponse> getAnnouncements(
      @RequestParam String sort,
      @RequestParam int page, @RequestParam int size) {

    return ResponseEntity.ok(
        announcementQueryService.getAnnouncements(Direction.fromString(sort), page, size));
  }
}
